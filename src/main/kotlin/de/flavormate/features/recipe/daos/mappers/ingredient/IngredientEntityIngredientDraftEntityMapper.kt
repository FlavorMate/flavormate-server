/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.daos.mappers.ingredient

import com.fasterxml.jackson.module.kotlin.convertValue
import de.flavormate.extensions.openFoodFacts.dao.models.OFFProductEntity
import de.flavormate.features.recipe.daos.models.NutritionEntity
import de.flavormate.features.recipe.daos.models.ingredient.IngredientEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftIngredientGroupItemNutritionEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupItemEntity
import de.flavormate.shared.interfaces.BasicMapper

object IngredientEntityIngredientDraftEntityMapper :
  BasicMapper<RecipeDraftIngredientGroupItemEntity, IngredientEntity>() {
  override fun mapNotNullBasic(input: RecipeDraftIngredientGroupItemEntity): IngredientEntity {
    return IngredientEntity().apply {
      this.amount = input.amount
      this.label = input.label!!
      this.index = input.index
      this.unit = om.convertValue(input.unit)
      this.nutrition = if (!input.nutrition.isEmpty) mapNutrition(input.nutrition) else null
    }
  }

  fun mapNutrition(input: RecipeDraftIngredientGroupItemNutritionEntity): NutritionEntity {
    return if (input.openFoodFactsId == null) {
      om.convertValue(input)
    } else {
      NutritionEntity().apply {
        this.openFoodFactsId = OFFProductEntity().apply { id = input.openFoodFactsId!! }
      }
    }
  }
}
