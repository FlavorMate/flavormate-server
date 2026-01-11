/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.configuration.properties.auth.oidc

import java.util.*

interface OIDCProperties {
  fun name(): String

  fun url(): String

  fun clientId(): String

  fun clientSecret(): Optional<String>

  fun id(): String

  fun redirectUriOverride(): Optional<Boolean>

  fun icon(): Optional<String>
}
