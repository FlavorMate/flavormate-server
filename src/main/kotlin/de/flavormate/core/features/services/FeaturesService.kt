/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.features.services

import de.flavormate.configuration.properties.FlavorMateProperties
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class FeaturesService(val flavorMateProperties: FlavorMateProperties) {

  fun getEnabledFeatures(): List<String> {
    return flavorMateProperties.features().filter { it.value.enabled() }.map { it.key.name }
  }
}
