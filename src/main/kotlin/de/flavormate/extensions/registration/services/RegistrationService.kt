/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.registration.services

import de.flavormate.core.auth.services.AuthTokenService
import de.flavormate.extensions.registration.controllers.RegistrationController
import de.flavormate.extensions.registration.models.RegistrationForm
import de.flavormate.extensions.urlShortener.services.ShortenerService
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.features.role.repositories.RoleRepository
import de.flavormate.shared.services.AccountCreateService
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.TemplateService
import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.UriBuilder
import java.time.Duration

@RequestScoped
class RegistrationService(
  val accountRepository: AccountRepository,
  val authorizationDetails: AuthorizationDetails,
  var mailer: Mailer,
  val roleRepository: RoleRepository,
  val tokenService: AuthTokenService,
  val shortenerService: ShortenerService,
  val templateService: TemplateService,
  val accountCreateService: AccountCreateService,
) {

  @Location("registration/welcome.html") private lateinit var emailTemplate: Template

  @Location("registration/ok.html") private lateinit var successTemplate: Template

  private val verifyTokenExpiration = Duration.parse("PT5M")

  @Transactional
  fun register(form: RegistrationForm): Boolean {
    val account =
      accountCreateService.createAccount(
        displayName = form.displayName,
        username = form.username,
        password = form.password,
        email = form.email,
      )

      val token = tokenService.createAndSaveVerifyToken(account)

      val path = UriBuilder.fromResource(RegistrationController::class.java)
          .path(RegistrationController::class.java, RegistrationController::verifyAccount.name)
          .queryParam("token", token).build().toString();

      val shortUrl = shortenerService.generateUrl(path)

    val data =
      mutableMapOf<String, Any?>(
        "displayName" to account.displayName,
        "shortUrl" to shortUrl,
        "expiresInMinutes" to verifyTokenExpiration.toMinutes(),
      )

    val html = templateService.handleTemplate(emailTemplate, data).render()

    val mail =
      Mail.withHtml(form.email, templateService.getMessage { it.verifyMail_subject() }, html)

    mailer.send(mail)

    return true
  }

  @Transactional
  fun verifyAccount(): TemplateInstance {
    val account = authorizationDetails.getSelf()
    account.verified = true

    tokenService.revokeJWT(authorizationDetails.token)

    return templateService.handleTemplate(successTemplate)
  }
}
