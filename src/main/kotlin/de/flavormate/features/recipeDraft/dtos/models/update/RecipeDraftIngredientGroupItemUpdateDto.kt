/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.dtos.models.update

import java.util.*

data class RecipeDraftIngredientGroupItemUpdateDto(
  val id: String,
  val index: Int?,
  val amount: Optional<Double>?,
  val label: Optional<String>?,
  val unit: Optional<String>?,
  val nutrition: RecipeDraftIngredientGroupItemNutritionUpdateDto?,

  // CRUD flag
  val delete: Boolean?,
)
