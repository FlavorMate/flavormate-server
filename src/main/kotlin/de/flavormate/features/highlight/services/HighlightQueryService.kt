/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.highlight.services

import de.flavormate.features.highlight.dtos.mappers.HighlightDtoMapper
import de.flavormate.features.highlight.dtos.models.HighlightDto
import de.flavormate.features.highlight.repositories.HighlightRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class HighlightQueryService(private val repository: HighlightRepository) {
  fun getHighlights(diet: Diet, pagination: Pagination): PageableDto<HighlightDto> {
    val dataQuery =
      repository.findAllByDiet(
        diet = diet,
        sort = pagination.sortRequest(map = AllowedSorts.highlights),
      )

    val countQuery = repository.countAllByDiet(diet = diet)

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      countQuery = countQuery,
      page = pagination.pageRequest,
      mapper = HighlightDtoMapper::mapNotNullBasic,
    )
  }
}
