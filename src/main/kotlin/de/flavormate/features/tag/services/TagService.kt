/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.tag.services

import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class TagService(private val queryService: TagQueryService) {

  fun getTags(pagination: Pagination) = queryService.getTags(pagination = pagination)

  fun getTag(id: String) = queryService.getTag(id = id)

  fun getTagRecipes(id: String, pagination: Pagination) =
    queryService.getTagRecipes(id = id, pagination = pagination)

  fun getTagsSearch(query: String, pagination: Pagination) =
    queryService.getTagsSearch(query = query, pagination = pagination)
}
