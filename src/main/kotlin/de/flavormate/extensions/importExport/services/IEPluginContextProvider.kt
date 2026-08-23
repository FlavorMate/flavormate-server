/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.services

import de.flavormate.configuration.jackson.CustomObjectMapper
import de.flavormate.shared.services.AuthorizationDetails
import io.quarkus.runtime.configuration.MemorySize
import jakarta.enterprise.context.RequestScoped
import org.eclipse.microprofile.config.inject.ConfigProperty

@RequestScoped
class IEPluginContextProvider(private val authorizationDetails: AuthorizationDetails) {
  @ConfigProperty(name = "quarkus.http.limits.max-body-size")
  private lateinit var maxBodySize: MemorySize

  val currentUser
    get() = authorizationDetails.account

  val maxImageSize
    get() = maxBodySize

  val objectMapper
    get() = CustomObjectMapper.ieInstance
}
