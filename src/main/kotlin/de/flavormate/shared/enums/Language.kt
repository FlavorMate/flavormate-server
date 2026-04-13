/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class Language(@JsonValue val value: String) {
  DE("de"),
  EN("en");

  companion object {
    fun from(value: String) = entries.find { it.value.equals(value, ignoreCase = true) }
  }
}
