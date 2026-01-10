/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.dtos.models

import de.flavormate.features.unit.dtos.models.UnitLocalizedDto

data class RecipeDraftIngredientGroupItemDto(
  val id: String,
  val index: Int,
  val label: String?,
  val amount: Double? = null,
  val unit: UnitLocalizedDto? = null,
  val nutrition: RecipeDraftIngredientGroupItemNutritionDto? = null,
)
