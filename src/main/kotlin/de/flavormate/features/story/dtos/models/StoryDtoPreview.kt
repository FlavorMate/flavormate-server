/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.story.dtos.models

import de.flavormate.features.recipe.dtos.models.RecipeFileDtoPreview

data class StoryDtoPreview(
  override val id: String,
  override val label: String,
  override val cover: RecipeFileDtoPreview?,
) : StoryDto
