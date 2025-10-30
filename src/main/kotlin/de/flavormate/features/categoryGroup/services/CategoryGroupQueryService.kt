/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.categoryGroup.services

import de.flavormate.features.categoryGroup.dtos.mappers.CategoryGroupDtoMapper
import de.flavormate.features.categoryGroup.dtos.models.CategoryGroupDto
import de.flavormate.features.categoryGroup.repositories.CategoryGroupRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class CategoryGroupQueryService(private val categoryGroupRepository: CategoryGroupRepository) {

  fun getCategoryGroups(language: String, pagination: Pagination): PageableDto<CategoryGroupDto> {
    val dataQuery =
      categoryGroupRepository.findAll(sort = pagination.sortRequest(AllowedSorts.categoryGroups))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = { CategoryGroupDtoMapper.mapNotNullL10n(it, language) },
    )
  }
}
