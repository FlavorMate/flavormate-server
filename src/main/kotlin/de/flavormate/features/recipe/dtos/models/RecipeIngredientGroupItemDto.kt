/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.dtos.models

import de.flavormate.features.unit.dtos.models.UnitLocalizedDto

data class RecipeIngredientGroupItemDto(
  val id: String,
  val label: String,
  val index: Int,
  val amount: Double? = null,
  val unit: UnitLocalizedDto? = null,
  val nutrition: RecipeIngredientGroupItemNutritionDto? = null,
)
