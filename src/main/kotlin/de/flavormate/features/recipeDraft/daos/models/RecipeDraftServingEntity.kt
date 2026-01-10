/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.daos.models

import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "v3__recipe_draft__serving")
class RecipeDraftServingEntity : CoreEntity() {
  var amount: Double? = null
  var label: String? = null

  companion object {
    fun create(): RecipeDraftServingEntity = RecipeDraftServingEntity()
  }
}
