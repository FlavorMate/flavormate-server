/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.dtos.models

data class RecipeDraftInstructionGroupDto(
  val id: String,
  val index: Int,
  val instructions: List<RecipeDraftInstructionGroupItemDto>,
  val label: String? = null,
)
