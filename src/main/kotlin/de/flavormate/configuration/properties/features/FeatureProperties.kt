/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.configuration.properties.features

import io.smallrye.config.WithDefault

interface FeatureProperties {
  @WithDefault("false") fun enabled(): Boolean
}
