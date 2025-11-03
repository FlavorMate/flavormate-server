/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.services

import de.flavormate.features.recipe.dtos.models.RecipeTransferDto
import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.enums.ImageWideResolution
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional

@RequestScoped
class RecipeService(
  private val mutationService: RecipeMutationService,
  private val queryService: RecipeQueryService,
) {
  fun getRecipes(pagination: Pagination) = queryService.getRecipes(pagination = pagination)

  fun streamCover(id: String, resolution: ImageWideResolution) =
    queryService.streamCover(id = id, resolution = resolution)

  fun streamFile(id: String, file: String, resolution: ImageWideResolution) =
    queryService.streamFile(id = id, file = file, resolution = resolution)

  fun getRecipe(id: String, language: String) = queryService.getRecipe(id = id, language = language)

  fun getRecipesIdIsInBook(recipeId: String, bookId: String) =
    queryService.getRecipesIdIsInBook(recipeId = recipeId, bookId = bookId)

  fun getRecipesRandom(diet: Diet, course: Course?, pagination: Pagination) =
    queryService.getRecipesRandom(diet = diet, course = course, pagination = pagination)

  fun getRecipesSearch(query: String, pagination: Pagination) =
    queryService.getRecipesSearch(query = query, pagination = pagination)

  fun getRecipesFiles(id: String, pagination: Pagination) =
    queryService.getRecipesFiles(id = id, pagination = pagination)

  @Transactional fun postRecipesId(id: String) = mutationService.createDraft(id = id)

  @Transactional fun deleteRecipesId(id: String) = mutationService.delete(id = id)

  @Transactional
  fun putRecipesIdTransfer(id: String, form: RecipeTransferDto) =
    mutationService.putRecipesIdTransfer(id = id, form = form)
}
