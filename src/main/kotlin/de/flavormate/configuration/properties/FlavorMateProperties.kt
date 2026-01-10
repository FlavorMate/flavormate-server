/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.configuration.properties

import de.flavormate.configuration.properties.auth.AuthProperties
import de.flavormate.configuration.properties.features.FeatureProperties
import de.flavormate.configuration.properties.general.GeneralProperties
import de.flavormate.configuration.properties.paths.PathsProperties
import de.flavormate.configuration.properties.server.ServerProperties
import de.flavormate.core.features.enums.FeatureType
import io.smallrye.config.ConfigMapping
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
@ConfigMapping(prefix = "flavormate")
interface FlavorMateProperties {
  fun auth(): AuthProperties

  fun paths(): PathsProperties

  fun features(): Map<FeatureType, FeatureProperties>

  fun general(): GeneralProperties

  fun server(): ServerProperties
}
