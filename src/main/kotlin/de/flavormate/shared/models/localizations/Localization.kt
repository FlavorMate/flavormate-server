/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.models.localizations

import jakarta.persistence.EmbeddedId
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class Localization {
  @EmbeddedId lateinit var id: LocalizationId

  lateinit var value: String
}
