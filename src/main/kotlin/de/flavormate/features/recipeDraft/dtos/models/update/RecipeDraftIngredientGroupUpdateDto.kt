/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.dtos.models.update

import java.util.*

data class RecipeDraftIngredientGroupUpdateDto(
  val id: String,
  val label: Optional<String>?,
  val index: Int?,
  val ingredients: List<RecipeDraftIngredientGroupItemUpdateDto>?,

  // CRUD flag
  val delete: Boolean?,
)
