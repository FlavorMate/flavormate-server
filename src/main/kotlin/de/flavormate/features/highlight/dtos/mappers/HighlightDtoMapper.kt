/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.highlight.dtos.mappers

import de.flavormate.features.highlight.daos.models.HighlightEntity
import de.flavormate.features.highlight.dtos.models.HighlightDto
import de.flavormate.features.recipe.dtos.mappers.RecipeDtoPreviewMapper
import de.flavormate.features.recipe.dtos.mappers.RecipeFileDtoPreviewMapper
import de.flavormate.shared.interfaces.BasicMapper

object HighlightDtoMapper : BasicMapper<HighlightEntity, HighlightDto>() {
  override fun mapNotNullBasic(input: HighlightEntity) =
    HighlightDto(
      id = input.id,
      date = input.date,
      diet = input.diet,
      recipe = RecipeDtoPreviewMapper.mapNotNullBasic(input.recipe),
      cover = input.recipe.coverFile?.let(RecipeFileDtoPreviewMapper::mapNotNullBasic),
    )
}
