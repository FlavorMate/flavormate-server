/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.dtos.models

data class RecipeInstructionGroupDto(
  val id: String,
  val index: Int,
  val instructions: List<RecipeInstructionGroupItemDto>,
  val label: String? = null,
)
