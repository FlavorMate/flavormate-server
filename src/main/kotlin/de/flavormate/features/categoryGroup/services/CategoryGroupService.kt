/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.categoryGroup.services

import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class CategoryGroupService(private val queryService: CategoryGroupQueryService) {

  fun getCategoryGroups(language: String, pagination: Pagination) =
    queryService.getCategoryGroups(language, pagination)
}
