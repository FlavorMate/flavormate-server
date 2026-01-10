/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.unit.services

import de.flavormate.features.unit.dtos.mappers.UnitLocalizedDtoMapper
import de.flavormate.features.unit.dtos.models.UnitLocalizedDto
import de.flavormate.features.unit.repositories.UnitLocalizedRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class UnitQueryService(private val repository: UnitLocalizedRepository) {
  fun getUnitsByLanguage(language: String, pagination: Pagination): PageableDto<UnitLocalizedDto> {
    val dataQuery =
      repository.findByLanguage(
        sort = pagination.sortRequest(map = AllowedSorts.units),
        language = language,
      )

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = UnitLocalizedDtoMapper::mapNotNullBasic,
    )
  }
}
