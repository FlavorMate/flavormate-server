/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.unit.daos.models

import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "v3__unit_ref")
class UnitRefEntity : CoreEntity() {
  lateinit var description: String
}
