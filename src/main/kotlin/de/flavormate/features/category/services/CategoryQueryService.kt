/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.category.services

import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.category.dtos.mappers.CategoryDtoMapper
import de.flavormate.features.category.dtos.models.CategoryDto
import de.flavormate.features.category.repositories.CategoryRepository
import de.flavormate.features.recipe.dtos.mappers.RecipeDtoPreviewMapper
import de.flavormate.features.recipe.dtos.models.RecipeDtoPreview
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class CategoryQueryService(private val categoryRepository: CategoryRepository) {

  fun getCategories(language: String, pagination: Pagination): PageableDto<CategoryDto> {
    val dataQuery =
      categoryRepository.findAll(
        sort = pagination.sortRequest(map = AllowedSorts.categories),
        language = language,
      )

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = { CategoryDtoMapper.mapNotNullL10n(input = it, language = language) },
    )
  }

  fun getCategory(id: String, language: String): CategoryDto {
    val category =
      categoryRepository.findById(id = id)
        ?: throw FNotFoundException(message = "Category with id $id not found")

    return CategoryDtoMapper.mapNotNullL10n(input = category, language = language)
  }

  fun getCategoryRecipes(id: String, pagination: Pagination): PageableDto<RecipeDtoPreview> {
    val dataQuery =
      categoryRepository.findRecipes(
        sort = pagination.sortRequest(map = AllowedSorts.recipes),
        id = id,
      )
    val countQuery = categoryRepository.countRecipes(id = id)

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      countQuery = countQuery,
      mapper = RecipeDtoPreviewMapper::mapNotNullBasic,
    )
  }

  fun getCategoriesSearch(
    query: String,
    language: String,
    pagination: Pagination,
  ): PageableDto<CategoryDto> {
    val dataQuery =
      categoryRepository.findBySearch(
        query = query,
        language = language,
        sort = pagination.sortRequest(AllowedSorts.recipes),
      )

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = { CategoryDtoMapper.mapNotNullL10n(input = it, language = language) },
    )
  }
}
