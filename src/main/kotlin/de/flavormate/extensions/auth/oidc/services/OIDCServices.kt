/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.services

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.core.auth.models.LoginForm
import de.flavormate.core.auth.models.TokenResponseDao
import de.flavormate.core.auth.services.AuthService
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.auth.oidc.controllers.OIDCController
import de.flavormate.extensions.auth.oidc.dao.models.OIDCMappingEntity
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
import kotlin.jvm.optionals.getOrNull
import org.apache.commons.io.FilenameUtils

@RequestScoped
class OIDCServices(
  private val accountRepository: AccountRepository,
  private val authService: AuthService,
  private val authorizationDetails: AuthorizationDetails,
  private val flavorMateProperties: FlavorMateProperties,
  private val oidcMappingRepository: OIDCMappingRepository,
) {
  fun login(): TokenResponseDao? {
    val issuer = authorizationDetails.issuer
    val subject = authorizationDetails.subject

    val mapping = oidcMappingRepository.findByJwt(issuer, subject) ?: return null

    return authService.loginOIDC(accountId = mapping.accountId)
  }

  fun getProviders(): List<OIDCProviderDto> {
    return flavorMateProperties.auth().oidc().map {
      val path =
        UriBuilder.fromResource(OIDCController::class.java)
          .path(OIDCController::class.java, OIDCController::getIcon.name)
          .build(it.id())
          .toString()

      OIDCProviderDto(
        url = it.url(),
        clientId = it.clientId(),
        name = it.name(),
        id = it.id(),
        iconPath = path,
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
}
