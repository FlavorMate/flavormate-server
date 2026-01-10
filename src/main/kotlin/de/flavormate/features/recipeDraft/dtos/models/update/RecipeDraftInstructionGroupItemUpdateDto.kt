/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.dtos.models.update

import java.util.*

data class RecipeDraftInstructionGroupItemUpdateDto(
  val id: String,
  val index: Int?,
  val label: Optional<String>?,

  // CRUD flag
  val delete: Boolean?,
)
