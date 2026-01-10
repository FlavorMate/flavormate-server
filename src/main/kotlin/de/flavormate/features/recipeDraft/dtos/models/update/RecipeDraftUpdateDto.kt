/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.dtos.models.update

import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import java.time.Duration
import java.util.*

data class RecipeDraftUpdateDto(
  val cookTime: Duration?,
  val course: Course?,
  val description: Optional<String>?,
  val diet: Diet?,
  val label: Optional<String>?,
  val prepTime: Duration?,
  val restTime: Duration?,
  val serving: RecipeDraftServingUpdateDto?,
  val instructionGroups: List<RecipeDraftInstructionGroupUpdateDto>?,
  val ingredientGroups: List<RecipeDraftIngredientGroupUpdateDto>?,
  val categories: List<String>?,
  val tags: List<String>?,
  val url: Optional<String>?,
)
