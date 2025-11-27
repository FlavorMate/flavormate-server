/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.ratings.services

import de.flavormate.extensions.ratings.dtos.RecipeRatingFormDto
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional

@RequestScoped
class RatingsService(
  private val mutationService: RatingsMutationService,
  private val queryService: RatingsQueryService,
) {
  fun getRecipesIdRating(recipeId: String) = queryService.getRecipesIdRating(recipeId = recipeId)

  @Transactional
  fun putRecipesIdRating(recipeId: String, form: RecipeRatingFormDto) =
    mutationService.putRecipesIdRating(recipeId = recipeId, form = form)

  @Transactional
  fun deleteRecipesIdRating(recipeId: String) =
    mutationService.deleteRecipesIdRating(recipeId = recipeId)
}
