/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.mapper

import de.flavormate.features.recipe.daos.models.ingredient.IngredientEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftIngredientGroupItemNutritionEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupItemEntity
import de.flavormate.shared.interfaces.BasicMapper

object IngredientDraftEntityIngredientEntityMapper :
  BasicMapper<IngredientEntity, RecipeDraftIngredientGroupItemEntity>() {
  override fun mapNotNullBasic(input: IngredientEntity): RecipeDraftIngredientGroupItemEntity {
    return RecipeDraftIngredientGroupItemEntity().apply {
      this.index = input.index
      this.amount = input.amount
      this.label = input.label
      this.unit = input.unit
      this.nutrition =
        input.nutrition?.let(NutritionDraftEntityNutritionEntityMapper::mapNotNullBasic)
          ?: RecipeDraftIngredientGroupItemNutritionEntity()
    }
  }
}
