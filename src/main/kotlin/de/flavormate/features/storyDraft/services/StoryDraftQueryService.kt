/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.storyDraft.services

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.storyDraft.dtos.mappers.StoryDraftDtoFullMapper
import de.flavormate.features.storyDraft.dtos.mappers.StoryDraftDtoPreviewMapper
import de.flavormate.features.storyDraft.dtos.models.StoryDraftDtoFull
import de.flavormate.features.storyDraft.dtos.models.StoryDraftDtoPreview
import de.flavormate.features.storyDraft.repositories.StoryDraftRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class StoryDraftQueryService(
  private val repository: StoryDraftRepository,
  private val authorizationDetails: AuthorizationDetails,
) {
  fun getStoryDrafts(pagination: Pagination): PageableDto<StoryDraftDtoPreview> {
    val self = authorizationDetails.getSelf()
    val dataQuery =
      repository.findAllByOwnedBy(
        id = self.id,
        sort = pagination.sortRequest(map = AllowedSorts.storyDrafts),
      )

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = StoryDraftDtoPreviewMapper::mapNotNullBasic,
    )
  }

  fun getStoryDraft(id: String): StoryDraftDtoFull {
    val entity =
      repository.findById(id)
        ?: throw FNotFoundException(message = "Story draft with id $id not found")

    if (!authorizationDetails.isAdminOrOwner(entity))
      throw FForbiddenException(message = "You are not allowed to access this recipe draft!")

    return StoryDraftDtoFullMapper.mapNotNullBasic(input = entity)
  }
}
