/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.unit.daos.models

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "v3__unit_conversion")
class UnitConversionEntity {
  @EmbeddedId lateinit var id: UnitConversionId

  var factor = 0.0
}
