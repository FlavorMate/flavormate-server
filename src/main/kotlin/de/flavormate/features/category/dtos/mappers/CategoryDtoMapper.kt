/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.category.dtos.mappers

import de.flavormate.features.category.daos.models.CategoryEntity
import de.flavormate.features.category.dtos.models.CategoryDto
import de.flavormate.features.recipe.dtos.mappers.RecipeFileDtoPreviewMapper
import de.flavormate.shared.interfaces.L10nMapper

object CategoryDtoMapper : L10nMapper<CategoryEntity, CategoryDto>() {
  override fun mapNotNullL10n(input: CategoryEntity, language: String): CategoryDto {
    input.translate(language)
    return CategoryDto(
      id = input.id,
      label = input.label,
      recipeCount = input.recipes.size,
      cover = input.coverRecipe?.coverFile?.let(RecipeFileDtoPreviewMapper::mapNotNullBasic),
    )
  }
}
