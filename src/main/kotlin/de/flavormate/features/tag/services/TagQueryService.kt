/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.tag.services

import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.recipe.dtos.mappers.RecipeDtoPreviewMapper
import de.flavormate.features.recipe.dtos.models.RecipeDtoPreview
import de.flavormate.features.tag.dtos.mappers.TagDtoMapper
import de.flavormate.features.tag.dtos.models.TagDto
import de.flavormate.features.tag.repositories.TagRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class TagQueryService(private val repository: TagRepository) {

  fun getTags(pagination: Pagination): PageableDto<TagDto> {
    val dataQuery = repository.findAll(sort = pagination.sortRequest(map = AllowedSorts.tags))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = TagDtoMapper::mapNotNullBasic,
    )
  }

  fun getTag(id: String): TagDto {
    val entity =
      repository.findById(id) ?: throw FNotFoundException(message = "Tag with id $id not found")

    return TagDtoMapper.mapNotNullBasic(entity)
  }

  fun getTagRecipes(id: String, pagination: Pagination): PageableDto<RecipeDtoPreview> {
    val dataQuery =
      repository.findRecipesById(id = id, sort = pagination.sortRequest(map = AllowedSorts.recipes))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = RecipeDtoPreviewMapper::mapNotNullBasic,
    )
  }

  fun getTagsSearch(query: String, pagination: Pagination): PageableDto<TagDto> {
    val dataQuery =
      repository.findBySearch(query = query, sort = pagination.sortRequest(AllowedSorts.tags))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = TagDtoMapper::mapNotNullBasic,
    )
  }
}
