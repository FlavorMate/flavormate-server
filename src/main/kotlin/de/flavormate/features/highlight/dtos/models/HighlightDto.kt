/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.highlight.dtos.models

import de.flavormate.features.recipe.dtos.models.RecipeDtoPreview
import de.flavormate.features.recipe.dtos.models.RecipeFileDtoPreview
import de.flavormate.shared.enums.Diet
import java.time.LocalDate

data class HighlightDto(
  val id: String,
  val date: LocalDate,
  val diet: Diet,
  val recipe: RecipeDtoPreview,
  val cover: RecipeFileDtoPreview? = null,
)
