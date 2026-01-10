/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.storyDraft.services

import de.flavormate.features.storyDraft.dtos.models.StoryDraftUpdateDto
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional

@RequestScoped
class StoryDraftService(
  private val mutationService: StoryDraftMutationService,
  private val queryService: StoryDraftQueryService,
) {
  // Queries
  fun getStoryDrafts(pagination: Pagination) = queryService.getStoryDrafts(pagination = pagination)

  fun getStoryDraftsId(id: String) = queryService.getStoryDraft(id = id)

  // Mutations
  @Transactional fun postStoryDrafts() = mutationService.postStoryDrafts()

  @Transactional fun deleteStoryDraftsId(id: String) = mutationService.deleteStoryDraftsId(id = id)

  @Transactional
  fun putStoryDraftsId(id: String, form: StoryDraftUpdateDto) =
    mutationService.putStoryDraftsId(id = id, form = form)

  @Transactional fun postStoryDraftsId(id: String) = mutationService.postStoryDraftsId(id = id)
}
