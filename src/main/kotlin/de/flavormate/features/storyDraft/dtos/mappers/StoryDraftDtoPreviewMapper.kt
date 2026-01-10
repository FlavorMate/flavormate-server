/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.storyDraft.dtos.mappers

import de.flavormate.features.recipe.dtos.mappers.RecipeDtoPreviewMapper
import de.flavormate.features.recipe.dtos.mappers.RecipeFileDtoPreviewMapper
import de.flavormate.features.storyDraft.daos.models.StoryDraftEntity
import de.flavormate.features.storyDraft.dtos.models.StoryDraftDtoPreview
import de.flavormate.shared.interfaces.BasicMapper

object StoryDraftDtoPreviewMapper : BasicMapper<StoryDraftEntity, StoryDraftDtoPreview>() {
  override fun mapNotNullBasic(input: StoryDraftEntity) =
    StoryDraftDtoPreview(
      id = input.id,
      label = input.label,
      recipe = input.recipe?.let(RecipeDtoPreviewMapper::mapNotNullBasic),
      cover = input.recipe?.coverFile?.let(RecipeFileDtoPreviewMapper::mapNotNullBasic),
      originId = input.originId,
    )
}
