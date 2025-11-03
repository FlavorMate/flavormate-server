/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.storyDraft.dtos.models

import de.flavormate.features.recipe.dtos.models.RecipeDtoPreview
import de.flavormate.features.recipe.dtos.models.RecipeFileDtoPreview

interface StoryDraftDto {
  val id: String
  val label: String?
  val recipe: RecipeDtoPreview?
  val cover: RecipeFileDtoPreview?
  val originId: String?
}
