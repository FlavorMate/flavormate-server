/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.daos.mappers

import de.flavormate.features.recipe.daos.models.ServingEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftServingEntity
import de.flavormate.shared.interfaces.BasicMapper

object ServingEntityServingDraftEntityMapper :
  BasicMapper<RecipeDraftServingEntity, ServingEntity>() {
  override fun mapNotNullBasic(input: RecipeDraftServingEntity): ServingEntity {
    return ServingEntity().apply {
      this.amount = input.amount!!
      this.label = input.label!!
    }
  }
}
