/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.config

import de.flavormate.configuration.properties.FlavorMateProperties
import io.quarkus.arc.Unremovable
import io.quarkus.logging.Log
import io.quarkus.oidc.client.OidcClient
import io.quarkus.oidc.client.OidcClients
import io.quarkus.oidc.client.runtime.OidcClientConfig
import io.quarkus.runtime.Startup
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import java.util.concurrent.ConcurrentHashMap
import kotlin.jvm.optionals.getOrNull

@ApplicationScoped
@Unremovable
@Startup
class OIDCClients(
  private val flavorMateProperties: FlavorMateProperties,
  private val oidcClients: OidcClients,
) {

  private final val clients: ConcurrentHashMap<String, OidcClient> = ConcurrentHashMap()

  fun onStart(@Observes ev: StartupEvent) {
    if (clients.isNotEmpty()) return

    for (provider in flavorMateProperties.auth().oidc()) {
      Log.info("Configuring client for provider ${provider.name()}")
      val clientConfig =
        OidcClientConfig.authServerUrl(provider.url())
          .id(provider.clientId())
          .clientId(provider.clientId())
          .credentials(provider.clientSecret().getOrNull())
          .clientEnabled(true)
          .build()

      val client = oidcClients.newClient(clientConfig).await().indefinitely()
      clients[provider.id()] = client
    }
  }

  fun getClient(id: String): OidcClient? = clients[id]
}
