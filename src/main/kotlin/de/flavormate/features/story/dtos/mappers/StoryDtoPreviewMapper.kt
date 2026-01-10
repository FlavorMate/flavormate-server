/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.story.dtos.mappers

import de.flavormate.features.recipe.dtos.mappers.RecipeFileDtoPreviewMapper
import de.flavormate.features.story.daos.models.StoryEntity
import de.flavormate.features.story.dtos.models.StoryDtoPreview
import de.flavormate.shared.interfaces.BasicMapper

object StoryDtoPreviewMapper : BasicMapper<StoryEntity, StoryDtoPreview>() {
  override fun mapNotNullBasic(input: StoryEntity) =
    StoryDtoPreview(
      id = input.id,
      label = input.label,
      cover = input.recipe.coverFile?.let(RecipeFileDtoPreviewMapper::mapNotNullBasic),
    )
}
