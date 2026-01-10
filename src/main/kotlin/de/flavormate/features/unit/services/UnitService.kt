/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.unit.services

import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class UnitService(private val queryService: UnitQueryService) {
  fun getUnitsByLanguage(language: String, pagination: Pagination) =
    queryService.getUnitsByLanguage(language = language, pagination = pagination)
}
