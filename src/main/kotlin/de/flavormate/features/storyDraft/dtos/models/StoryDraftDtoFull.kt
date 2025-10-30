/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.storyDraft.dtos.models

import de.flavormate.features.account.dtos.models.AccountPreviewDto
import de.flavormate.features.recipe.dtos.models.RecipeDtoPreview
import de.flavormate.features.recipe.dtos.models.RecipeFileDtoPreview

data class StoryDraftDtoFull(
  override val id: String,
  override val label: String?,
  override val recipe: RecipeDtoPreview?,
  override val cover: RecipeFileDtoPreview?,
  override val originId: String?,
  val content: String?,
  val ownedBy: AccountPreviewDto,
) : StoryDraftDto
