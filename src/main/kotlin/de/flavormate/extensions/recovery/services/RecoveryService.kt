/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.recovery.services

import de.flavormate.core.auth.services.AuthTokenService
import de.flavormate.exceptions.FBadRequestException
import de.flavormate.extensions.recovery.controllers.RecoveryController
import de.flavormate.extensions.urlShortener.services.ShortenerService
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.TemplateService
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.UriBuilder
import java.time.Duration
import org.jboss.resteasy.reactive.RestForm

/**
 * RecoveryService handles user account recovery-related functionalities. Provides endpoints to
 * initiate and complete password reset procedures.
 *
 * @param accountRepository Repository for accessing account-related data.
 * @param authorizationDetails Provides details about the currently authenticated user.
 * @param mailer Service used to send emails.
 * @param shortenerService Service to generate shortened URLs.
 * @param templateService Service to render HTML templates.
 * @param tokenService Service to manage authentication tokens.
 * @constructor Creates a RecoveryService object with required dependencies.
 */
@RequestScoped
@Path("/v3/recovery")
class RecoveryService(
  val accountRepository: AccountRepository,
  val authorizationDetails: AuthorizationDetails,
  val mailer: Mailer,
  val shortenerService: ShortenerService,
  val templateService: TemplateService,
  val tokenService: AuthTokenService,
) {
  // Templates
  @Location("recovery/password/request/recovery.html")
  private lateinit var recoveryTemplate: Template

  @Location("recovery/password/request/email.html") private lateinit var emailTemplate: Template

  @Location("recovery/password/request/ok.html") private lateinit var successTemplate: Template

  @Location("recovery/password/request/error.html") private lateinit var failureTemplate: Template

  private val resetTokenExpiration = Duration.parse("PT5M")

  /**
   * Initiates the password reset process for the specified email address. This method verifies the
   * account associated with the email, generates a reset token, creates a shortened URL for the
   * reset link, and sends a password recovery email to the user.
   *
   * @param email The email address of the user requesting the password reset.
   * @return A boolean value indicating whether the request was successfully processed. Returns
   *   `true` if the email was sent successfully.
   * @throws NotFoundException If no account is found associated with the provided email.
   */
  @Transactional
  fun requestPasswordReset(email: String): Boolean {
    val account = accountRepository.findByEmail(email) ?: return true

    val token = tokenService.createAndSaveResetToken(account)

    val path =
      UriBuilder.fromResource(RecoveryController::class.java)
        .path(RecoveryController::class.java, RecoveryController::handlePasswordReset.name)
        .queryParam("token", token)
        .build()
        .toString()

    val shortUrl = shortenerService.generateUrl(path)

    val data =
      mutableMapOf<String, Any?>(
        "displayName" to account.displayName,
        "username" to account.username,
        "shortUrl" to shortUrl,
        "expiresInMinutes" to resetTokenExpiration.toMinutes(),
      )

    val html = templateService.handleTemplate(emailTemplate, data).render()

    val mail =
      Mail.withHtml(email, templateService.getMessage { it.passwordRecoveryMail_subject() }, html)
    mailer.send(mail)

    return true
  }

  /**
   * Displays the password reset page to the user.
   *
   * This method renders the password reset page using a predefined template, providing a user
   * interface to request and handle the password reset process.
   *
   * @return A TemplateInstance representing the rendered password reset page.
   */
  fun showPasswordResetPage() = templateService.handleTemplate(recoveryTemplate)

  /**
   * Handles the password reset process by verifying the validity of the reset token, updating the
   * user's password securely, and rendering the appropriate response template.
   *
   * @param password The new password provided by the user for the reset process.
   * @return A TemplateInstance representing the result page, indicating success or failure. Returns
   *   the success template if the operation completes successfully, or the failure template in case
   *   of an error.
   */
  @Transactional
  fun handlePasswordReset(@RestForm password: String): TemplateInstance {
    try {
      if (!tokenService.revokeJWT(authorizationDetails.token))
        throw FBadRequestException("Invalid token")

      authorizationDetails.getSelf().password = BcryptUtil.bcryptHash(password)

      return templateService.handleTemplate(successTemplate)
    } catch (_: Exception) {
      return templateService.handleTemplate(failureTemplate)
    }
  }
}
