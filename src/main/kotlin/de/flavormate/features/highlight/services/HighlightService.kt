/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.highlight.services

import de.flavormate.shared.enums.Diet
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class HighlightService(private val queryService: HighlightQueryService) {
  fun getHighlights(diet: Diet, pagination: Pagination) =
    queryService.getHighlights(diet, pagination)
}
