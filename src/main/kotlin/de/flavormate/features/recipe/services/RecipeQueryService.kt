/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.services

import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.recipe.dtos.mappers.RecipeDtoFullMapper
import de.flavormate.features.recipe.dtos.mappers.RecipeDtoPreviewMapper
import de.flavormate.features.recipe.dtos.mappers.RecipeFileDtoPreviewMapper
import de.flavormate.features.recipe.dtos.models.RecipeDtoFull
import de.flavormate.features.recipe.dtos.models.RecipeDtoPreview
import de.flavormate.features.recipe.dtos.models.RecipeFileDtoPreview
import de.flavormate.features.recipe.repositories.RecipeFileRepository
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.enums.ImageWideResolution
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.FileService
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.core.StreamingOutput

@RequestScoped
class RecipeQueryService(
  private val fileService: FileService,
  private val repository: RecipeRepository,
  private val fileRepository: RecipeFileRepository,
) {
  fun getRecipes(pagination: Pagination): PageableDto<RecipeDtoPreview> {
    val dataQuery = repository.findAll(sort = pagination.sortRequest(map = AllowedSorts.recipes))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = RecipeDtoPreviewMapper::mapNotNullBasic,
    )
  }

  fun streamCover(id: String, resolution: ImageWideResolution): StreamingOutput {
    val recipe =
      repository.findById(id = id) ?: throw FNotFoundException(message = "Recipe not found")

    val cover = recipe.coverFile ?: throw FNotFoundException(message = "Recipe has no cover")

    return fileService.streamFile(
      prefix = FilePath.Recipe,
      uuid = cover.id,
      fileName = resolution.fileName,
    )
  }

  fun streamFile(id: String, file: String, resolution: ImageWideResolution): StreamingOutput {
    repository.findById(id = id) ?: throw FNotFoundException(message = "Recipe not found")

    val fileEntity =
      fileRepository.findById(id = file) ?: throw FNotFoundException(message = "File not found")

    return fileService.streamFile(
      prefix = FilePath.Recipe,
      uuid = fileEntity.id,
      fileName = resolution.fileName,
    )
  }

  fun getRecipe(id: String, language: String): RecipeDtoFull {
    val recipe = repository.findById(id) ?: throw FNotFoundException(message = "Recipe not found")
    return RecipeDtoFullMapper.mapNotNullL10n(input = recipe, language = language)
  }

  fun getRecipesRandom(
    diet: Diet,
    course: Course?,
    pagination: Pagination,
  ): PageableDto<RecipeDtoPreview> {
    val dataQuery = repository.findRandomRecipeByDiet(diets = diet.allowedDiets, course = course)
    val countQuery = repository.countRandomRecipeByDiet(diets = diet.allowedDiets, course = course)

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      countQuery = countQuery,
      page = pagination.pageRequest,
      mapper = RecipeDtoPreviewMapper::mapNotNullBasic,
    )
  }

  fun getRecipesSearch(query: String, pagination: Pagination): PageableDto<RecipeDtoPreview> {
    val dataQuery =
      repository.findBySearch(query = query, sort = pagination.sortRequest(AllowedSorts.recipes))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = RecipeDtoPreviewMapper::mapNotNullBasic,
    )
  }

  fun getRecipesFiles(id: String, pagination: Pagination): PageableDto<RecipeFileDtoPreview> {
    val dataQuery = repository.findFiles(id = id, sort = pagination.sortRequest(AllowedSorts.files))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = RecipeFileDtoPreviewMapper::mapNotNullBasic,
    )
  }

  fun getRecipesIdIsInBook(recipeId: String, bookId: String): Boolean {
    val recipe =
      repository.findById(recipeId) ?: throw FNotFoundException(message = "Recipe not found")

    return recipe.books.any { it.id == bookId }
  }
}
