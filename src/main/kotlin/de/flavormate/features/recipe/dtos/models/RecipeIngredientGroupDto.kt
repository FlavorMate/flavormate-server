/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.dtos.models

data class RecipeIngredientGroupDto(
  val id: String,
  val index: Int,
  val ingredients: List<RecipeIngredientGroupItemDto>,
  val label: String? = null,
)
