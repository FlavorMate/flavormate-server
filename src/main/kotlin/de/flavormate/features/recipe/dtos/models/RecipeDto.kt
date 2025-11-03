/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.dtos.models

import de.flavormate.shared.enums.Diet
import java.time.Duration
import java.time.LocalDateTime

interface RecipeDto {
  val id: String
  val createdOn: LocalDateTime
  val label: String
  val diet: Diet
  val cookTime: Duration
  val prepTime: Duration
  val restTime: Duration
  val cover: RecipeFileDtoPreview?
}
