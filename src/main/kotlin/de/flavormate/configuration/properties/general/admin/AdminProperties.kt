/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.configuration.properties.general.admin

interface AdminProperties {
  fun displayName(): String

  fun username(): String

  fun password(): String

  fun email(): String
}
