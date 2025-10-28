/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.mapper

import de.flavormate.features.recipe.daos.models.NutritionEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftIngredientGroupItemNutritionEntity
import de.flavormate.shared.interfaces.BasicMapper

object NutritionDraftEntityNutritionEntityMapper :
  BasicMapper<NutritionEntity, RecipeDraftIngredientGroupItemNutritionEntity>() {
  override fun mapNotNullBasic(
    input: NutritionEntity
  ): RecipeDraftIngredientGroupItemNutritionEntity {
    return RecipeDraftIngredientGroupItemNutritionEntity().apply {
      this.openFoodFactsId = input.openFoodFactsId?.id
      this.carbohydrates = input.carbohydrates
      this.energyKcal = input.energyKcal
      this.fat = input.fat
      this.saturatedFat = input.saturatedFat
      this.sugars = input.sugars
      this.fiber = input.fiber
      this.proteins = input.proteins
      this.salt = input.salt
      this.sodium = input.sodium
    }
  }
}
