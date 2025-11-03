/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.shared.models.localizations

import jakarta.persistence.Embeddable
import java.util.*

@Embeddable
class LocalizationId {
  lateinit var foreignId: String
  lateinit var language: String

  override fun hashCode(): Int {
    return Objects.hash(foreignId, language)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null) return false
    if (javaClass != other.javaClass) return false
    return Objects.hash(foreignId, language) == other.hashCode()
  }
}
