/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.story.services

import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional

@RequestScoped
class StoryService(
  private val mutationService: StoryMutationService,
  private val queryService: StoryQueryService,
) {
  // Queries
  fun getStories(pagination: Pagination) = queryService.getStories(pagination = pagination)

  fun getStoriesSearch(query: String, pagination: Pagination) =
    queryService.getStoriesSearch(query = query, pagination = pagination)

  fun getStoriesId(id: String) = queryService.getStoriesId(id = id)

  // Mutations
  @Transactional fun deleteStoriesId(id: String) = mutationService.deleteStoriesId(id = id)

  @Transactional fun editStoriesId(id: String) = mutationService.editStoriesId(id = id)
}
