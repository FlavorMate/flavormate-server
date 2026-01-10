/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.configuration.properties.auth

import de.flavormate.configuration.properties.auth.jwt.JWTProperties
import de.flavormate.configuration.properties.auth.oidc.OIDCProperties

interface AuthProperties {
  fun jwt(): JWTProperties

  fun oidc(): List<OIDCProperties>
}
