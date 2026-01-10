/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.dtos.models

data class RecipeDraftIngredientGroupDto(
  val id: String,
  val index: Int,
  val ingredients: List<RecipeDraftIngredientGroupItemDto>,
  val label: String? = null,
)
