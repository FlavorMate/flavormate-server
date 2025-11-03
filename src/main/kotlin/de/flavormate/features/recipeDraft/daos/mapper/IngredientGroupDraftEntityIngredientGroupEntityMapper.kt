/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.mapper

import de.flavormate.features.recipe.daos.models.ingredient.IngredientGroupEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupEntity
import de.flavormate.shared.interfaces.BasicMapper

object IngredientGroupDraftEntityIngredientGroupEntityMapper :
  BasicMapper<IngredientGroupEntity, RecipeDraftIngredientGroupEntity>() {
  override fun mapNotNullBasic(input: IngredientGroupEntity): RecipeDraftIngredientGroupEntity {
    return RecipeDraftIngredientGroupEntity().apply {
      this.label = input.label
      this.index = input.index
      this.ingredients =
        input.ingredients.mapTo(mutableListOf()) { ingredient ->
          IngredientDraftEntityIngredientEntityMapper.mapNotNullBasic(ingredient).also {
            it.group = this
          }
        }
    }
  }
}
