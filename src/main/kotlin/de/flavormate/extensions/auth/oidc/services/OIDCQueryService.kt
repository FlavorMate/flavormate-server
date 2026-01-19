/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.services

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.extensions.auth.oidc.controllers.OIDCController
import de.flavormate.extensions.auth.oidc.dto.mappers.OIDCMappingDtoOIDCMappingEntityMapper
import de.flavormate.extensions.auth.oidc.dto.models.OIDCMappingDto
import de.flavormate.extensions.auth.oidc.dto.models.OIDCProviderDto
import de.flavormate.extensions.auth.oidc.repositories.OIDCMappingRepository
import de.flavormate.extensions.auth.oidc.repositories.OIDCProviderRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.core.UriBuilder

@RequestScoped
class OIDCQueryService(
  private val authorizationDetails: AuthorizationDetails,
  private val flavorMateProperties: FlavorMateProperties,
  private val oidcMappingRepository: OIDCMappingRepository,
  private val oidcProviderRepository: OIDCProviderRepository,
) {

  fun getProviders(): List<OIDCProviderDto> {
    val providers = oidcProviderRepository.findAllEnabled()

    return providers.map {
      val redirectUriOverride =
        UriBuilder.fromUri(flavorMateProperties.server().url())
          .path(OIDCController::class.java)
          .path(OIDCController::class.java, OIDCController::mobileRedirect.name)
          .build()
          .toString()

      val redirectUri = if (it.overrideRedirectUri) redirectUriOverride else "flavormate://oauth"

      OIDCProviderDto(
        url = it.urlDiscoveryFullEndpoint,
        issuer = it.issuer,
        clientId = it.clientId,
        label = it.label,
        id = it.id,
        icon = it.icon,
        redirectUri = redirectUri,
      )
    }
  }

  fun getLinks(pagination: Pagination): PageableDto<OIDCMappingDto> {
    val self = authorizationDetails.getSelf()

    val query =
      oidcMappingRepository.findByAccountId(
        accountId = self.id,
        sort = pagination.sortRequest(AllowedSorts.oidc),
      )

    return PageableDto.fromQuery(
      dataQuery = query,
      page = pagination.pageRequest,
      mapper = OIDCMappingDtoOIDCMappingEntityMapper::mapNotNullBasic,
    )
  }
}
