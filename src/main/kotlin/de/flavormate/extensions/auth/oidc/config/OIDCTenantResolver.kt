/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.config

import de.flavormate.core.encryption.services.EncryptionService
import de.flavormate.extensions.auth.oidc.dao.models.OIDCProviderEntity
import de.flavormate.extensions.auth.oidc.repositories.OIDCProviderRepository
import io.quarkus.oidc.OidcRequestContext
import io.quarkus.oidc.OidcTenantConfig
import io.quarkus.oidc.TenantConfigResolver
import io.smallrye.jwt.auth.principal.JWTParser
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.infrastructure.Infrastructure
import io.vertx.ext.web.RoutingContext
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.control.ActivateRequestContext
import jakarta.ws.rs.core.HttpHeaders

@ApplicationScoped
class OIDCTenantResolver(
  private val jwtParser: JWTParser,
  private val oidcProviderRepository: OIDCProviderRepository,
  private val encryptionService: EncryptionService,
) : TenantConfigResolver {

  companion object {
    private const val BEARER_PREFIX = "Bearer "
  }

  override fun resolve(
    routingContext: RoutingContext?,
    requestContext: OidcRequestContext<OidcTenantConfig?>?,
  ): Uni<OidcTenantConfig?>? {

    return Uni.createFrom()
      .item { getProviderConfig(routingContext) }
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
      .map { config -> config?.let { createTenantConfig(it) } }
  }

  @ActivateRequestContext
  fun getProviderConfig(routingContext: RoutingContext?): OIDCProviderEntity? {
    val authHeader = routingContext?.request()?.getHeader(HttpHeaders.AUTHORIZATION) ?: return null

    if (!authHeader.startsWith(BEARER_PREFIX, ignoreCase = true)) return null

    val rawJwt = authHeader.substring(BEARER_PREFIX.length)
    val jwt = jwtParser.parseOnly(rawJwt)

    val issuer = jwt.issuer
    val audiences = jwt.audience

    return oidcProviderRepository.findByIssuerAndClientIds(issuer, audiences)
  }

  private fun createTenantConfig(config: OIDCProviderEntity): OidcTenantConfig {
    return OidcTenantConfig.authServerUrl(config.urlDiscoveryBasicEndpoint)
      .tenantId(config.id)
      .clientId(config.clientId)
      .credentials(config.clientSecret)
      .tenantEnabled(config.enabled)
      .build()
  }
}
