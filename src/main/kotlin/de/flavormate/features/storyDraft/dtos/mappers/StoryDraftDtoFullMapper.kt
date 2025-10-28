/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.storyDraft.dtos.mappers

import de.flavormate.features.account.dtos.mappers.AccountPreviewDtoMapper
import de.flavormate.features.recipe.dtos.mappers.RecipeDtoPreviewMapper
import de.flavormate.features.recipe.dtos.mappers.RecipeFileDtoPreviewMapper
import de.flavormate.features.storyDraft.daos.models.StoryDraftEntity
import de.flavormate.features.storyDraft.dtos.models.StoryDraftDtoFull
import de.flavormate.shared.interfaces.BasicMapper

object StoryDraftDtoFullMapper : BasicMapper<StoryDraftEntity, StoryDraftDtoFull>() {
  override fun mapNotNullBasic(input: StoryDraftEntity) =
    StoryDraftDtoFull(
      id = input.id,
      label = input.label,
      content = input.content,
      recipe = input.recipe?.let(RecipeDtoPreviewMapper::mapNotNullBasic),
      cover = input.recipe?.coverFile?.let(RecipeFileDtoPreviewMapper::mapNotNullBasic),
      originId = input.originId,
      ownedBy = AccountPreviewDtoMapper.mapNotNullBasic(input.ownedBy),
    )
}
