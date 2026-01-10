/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.story.dtos.models

import de.flavormate.features.account.dtos.models.AccountPreviewDto
import de.flavormate.features.recipe.dtos.models.RecipeDtoPreview
import de.flavormate.features.recipe.dtos.models.RecipeFileDtoPreview

data class StoryDtoFull(
  override val id: String,
  override val label: String,
  override val cover: RecipeFileDtoPreview?,
  val content: String,
  val ownedBy: AccountPreviewDto,
  val recipe: RecipeDtoPreview,
) : StoryDto
