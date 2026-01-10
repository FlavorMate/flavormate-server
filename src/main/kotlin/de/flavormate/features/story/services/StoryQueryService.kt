/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.story.services

import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.story.dtos.mappers.StoryDtoFullMapper
import de.flavormate.features.story.dtos.mappers.StoryDtoPreviewMapper
import de.flavormate.features.story.dtos.models.StoryDtoFull
import de.flavormate.features.story.dtos.models.StoryDtoPreview
import de.flavormate.features.story.repositories.StoryRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class StoryQueryService(private val repository: StoryRepository) {

  fun getStories(pagination: Pagination): PageableDto<StoryDtoPreview> {
    val dataQuery = repository.findAll(sort = pagination.sortRequest(AllowedSorts.stories))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = StoryDtoPreviewMapper::mapNotNullBasic,
    )
  }

  fun getStoriesId(id: String): StoryDtoFull {
    val entity =
      repository.findById(id) ?: throw FNotFoundException(message = "Story with id $id not found")

    return StoryDtoFullMapper.mapNotNullBasic(entity)
  }

  fun getStoriesSearch(query: String, pagination: Pagination): PageableDto<StoryDtoPreview> {
    val dataQuery =
      repository.findBySearch(query = query, sort = pagination.sortRequest(AllowedSorts.stories))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = StoryDtoPreviewMapper::mapNotNullBasic,
    )
  }
}
