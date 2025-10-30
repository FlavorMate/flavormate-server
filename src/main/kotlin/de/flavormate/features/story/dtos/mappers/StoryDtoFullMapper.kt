/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.story.dtos.mappers

import de.flavormate.features.account.dtos.mappers.AccountPreviewDtoMapper
import de.flavormate.features.recipe.dtos.mappers.RecipeDtoPreviewMapper
import de.flavormate.features.recipe.dtos.mappers.RecipeFileDtoPreviewMapper
import de.flavormate.features.story.daos.models.StoryEntity
import de.flavormate.features.story.dtos.models.StoryDtoFull
import de.flavormate.shared.interfaces.BasicMapper

object StoryDtoFullMapper : BasicMapper<StoryEntity, StoryDtoFull>() {
  override fun mapNotNullBasic(input: StoryEntity) =
    StoryDtoFull(
      id = input.id,
      label = input.label,
      content = input.content,
      ownedBy = AccountPreviewDtoMapper.mapNotNullBasic(input.ownedBy),
      recipe = RecipeDtoPreviewMapper.mapNotNullBasic(input.recipe),
      cover = input.recipe.coverFile?.let(RecipeFileDtoPreviewMapper::mapNotNullBasic),
    )
}
