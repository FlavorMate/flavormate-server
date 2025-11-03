/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.story.dtos.models

import de.flavormate.features.recipe.dtos.models.RecipeFileDtoPreview

interface StoryDto {
  val id: String
  val label: String
  val cover: RecipeFileDtoPreview?
}
