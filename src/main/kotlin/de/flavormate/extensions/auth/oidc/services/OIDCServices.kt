/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.services

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.core.auth.models.LoginForm
import de.flavormate.core.auth.models.TokenResponseDao
import de.flavormate.core.auth.services.AuthService
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.auth.oidc.config.OIDCClients
import de.flavormate.extensions.auth.oidc.controllers.OIDCController
import de.flavormate.extensions.auth.oidc.dao.models.OIDCMappingEntity
import de.flavormate.extensions.auth.oidc.dto.models.OIDCExchangeForm
import de.flavormate.extensions.auth.oidc.dto.models.OIDCProviderDto
import de.flavormate.extensions.auth.oidc.repositories.OIDCMappingRepository
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.utils.FileUtils
import de.flavormate.utils.MimeTypes
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.StreamingOutput
import jakarta.ws.rs.core.UriBuilder
import java.nio.file.Paths
import java.time.Duration
import kotlin.jvm.optionals.getOrNull
import org.apache.commons.io.FilenameUtils

@RequestScoped
class OIDCServices(
  private val accountRepository: AccountRepository,
  private val authService: AuthService,
  private val authorizationDetails: AuthorizationDetails,
  private val flavorMateProperties: FlavorMateProperties,
  private val oidcMappingRepository: OIDCMappingRepository,
  private val oidcClients: OIDCClients,
) {
  fun login(): TokenResponseDao? {
    val issuer = authorizationDetails.issuer
    val subject = authorizationDetails.subject

    val mapping = oidcMappingRepository.findByJwt(issuer, subject) ?: return null

    return authService.loginOIDC(accountId = mapping.accountId)
  }

  fun getProviders(): List<OIDCProviderDto> {
    return flavorMateProperties.auth().oidc().map {
      val redirectUriOverride =
        UriBuilder.fromUri(flavorMateProperties.server().url())
          .path(OIDCController::class.java)
          .path(OIDCController::class.java, OIDCController::mobileRedirect.name)
          .build()

      val path =
        UriBuilder.fromResource(OIDCController::class.java)
          .path(OIDCController::class.java, OIDCController::getIcon.name)
          .build(it.id())
          .toString()

      val redirectUri =
        if (it.redirectUriOverride().getOrNull() == true) redirectUriOverride.toString()
        else "flavormate://oauth"

      OIDCProviderDto(
        url = it.url() + ".well-known/openid-configuration",
        clientId = it.clientId(),
        name = it.name(),
        id = it.id(),
        iconPath = path,
        redirectUri = redirectUri,
      )
    }
  }

  fun getIcon(id: String): StreamingOutput? {
    val provider = flavorMateProperties.auth().oidc().firstOrNull { it.id() == id } ?: return null

    var icon = provider.icon().getOrNull() ?: return null
    icon = FilenameUtils.removeExtension(icon) + MimeTypes.WEBP_EXTENSION

    val fullPath =
      Paths.get(flavorMateProperties.paths().files())
        .resolve(FilePath.OIDCProvider.path)
        .resolve(icon)

    return FileUtils.streamFile(fullPath)
  }

  @Transactional
  fun link(form: LoginForm): TokenResponseDao {
    val account =
      accountRepository.findByUsername(form.username)
        ?: throw FNotFoundException(message = "Account with id ${form.username} not found")

    val token = authService.login(form)

    // At this point the credentials are valid
    val issuer = authorizationDetails.issuer
    val subject = authorizationDetails.subject
    OIDCMappingEntity()
      .apply {
        this.issuer = issuer
        this.subject = subject
        this.accountId = account.id
      }
      .also { oidcMappingRepository.persist(it) }

    return token
  }

  fun exchangeToken(form: OIDCExchangeForm): String {
    val client =
      oidcClients.getClient(form.providerId)
        ?: throw FNotFoundException(message = "Provider with id ${form.providerId} not found")

    val grantParameters =
      mutableMapOf(
        "grant_type" to "authorization_code",
        "code" to form.code,
        "code_verifier" to form.codeVerifier,
        "redirect_uri" to form.redirectUri,
      )

    val tokens = client.getTokens(grantParameters).await().atMost(Duration.ofMinutes(1))

    return tokens.get("id_token")
  }
}
