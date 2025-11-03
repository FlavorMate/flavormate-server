/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.configuration.properties.auth.oidc

import java.util.*

interface OIDCProperties {
  fun name(): String

  fun url(): String

  fun clientId(): String

  fun id(): String

  fun icon(): Optional<String>
}
