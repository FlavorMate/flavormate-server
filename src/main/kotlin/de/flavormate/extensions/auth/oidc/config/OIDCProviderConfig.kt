/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.auth.oidc.config

import de.flavormate.configuration.properties.FlavorMateProperties
import io.quarkus.logging.Log
import io.quarkus.oidc.Oidc
import io.quarkus.oidc.OidcTenantConfig
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes

@ApplicationScoped
class OIDCProviderConfig(private val flavorMateProperties: FlavorMateProperties) {

  fun observe(@Observes oidc: Oidc) {
    for (provider in flavorMateProperties.auth().oidc()) {
      Log.info("Configuring tenant for provider ${provider.name()}")
      val tenant =
        OidcTenantConfig.authServerUrl(provider.url())
          .tenantId(provider.id())
          .clientId(provider.clientId())
          .tenantEnabled(true)
          .build()

      oidc.create(tenant)
    }
  }
}
