/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.tag.dtos.mappers

import de.flavormate.features.recipe.dtos.mappers.RecipeFileDtoPreviewMapper
import de.flavormate.features.tag.daos.models.TagEntity
import de.flavormate.features.tag.dtos.models.TagDto
import de.flavormate.shared.interfaces.BasicMapper

object TagDtoMapper : BasicMapper<TagEntity, TagDto>() {
  override fun mapNotNullBasic(input: TagEntity) =
    TagDto(
      id = input.id,
      label = input.label,
      recipeCount = input.recipes.count(),
      cover = input.coverRecipe?.coverFile?.let(RecipeFileDtoPreviewMapper::mapNotNullBasic),
    )
}
