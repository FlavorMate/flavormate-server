/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.dtos.models

import de.flavormate.shared.enums.Diet
import java.time.Duration
import java.time.LocalDateTime

data class RecipeDtoPreview(
  override val id: String,
  override val createdOn: LocalDateTime,
  override val label: String,
  override val diet: Diet,
  override val cookTime: Duration,
  override val prepTime: Duration,
  override val restTime: Duration,
  override val cover: RecipeFileDtoPreview? = null,
) : RecipeDto
