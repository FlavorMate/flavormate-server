/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.categoryGroup.dtos.mappers

import de.flavormate.features.category.dtos.mappers.CategoryDtoMapper
import de.flavormate.features.categoryGroup.daos.models.CategoryGroupEntity
import de.flavormate.features.categoryGroup.dtos.models.CategoryGroupDto
import de.flavormate.shared.interfaces.L10nMapper

object CategoryGroupDtoMapper : L10nMapper<CategoryGroupEntity, CategoryGroupDto>() {
  override fun mapNotNullL10n(input: CategoryGroupEntity, language: String): CategoryGroupDto {
    input.translate(language)

    return CategoryGroupDto(
      id = input.id,
      label = input.label,
      categories =
        input.categories
          .map { CategoryDtoMapper.mapNotNullL10n(it, language) }
          .sortedBy { it.label },
    )
  }
}
