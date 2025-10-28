/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.mapper

import de.flavormate.features.recipe.daos.models.ServingEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftServingEntity
import de.flavormate.shared.interfaces.BasicMapper

object ServingDraftEntityServingEntityMapper :
  BasicMapper<ServingEntity, RecipeDraftServingEntity>() {
  override fun mapNotNullBasic(input: ServingEntity): RecipeDraftServingEntity {
    return RecipeDraftServingEntity().apply {
      this.label = input.label
      this.amount = input.amount
    }
  }
}
