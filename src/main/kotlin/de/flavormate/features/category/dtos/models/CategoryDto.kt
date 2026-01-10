/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.category.dtos.models

import de.flavormate.features.recipe.dtos.models.RecipeFileDtoPreview

data class CategoryDto(
  val id: String,
  val label: String,
  val recipeCount: Int,
  val cover: RecipeFileDtoPreview?,
)
