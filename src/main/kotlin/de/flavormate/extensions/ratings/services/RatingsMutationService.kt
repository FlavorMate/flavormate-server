/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.ratings.services

import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.ratings.daos.RatingEntity
import de.flavormate.extensions.ratings.dtos.RecipeRatingFormDto
import de.flavormate.features.recipe.repositories.RecipeRatingRepository
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class RatingsMutationService(
  private val authorizationDetails: AuthorizationDetails,
  private val recipeRepository: RecipeRepository,
  private val ratingRepository: RecipeRatingRepository,
) {

  fun putRecipesIdRating(recipeId: String, form: RecipeRatingFormDto) {
    val recipe =
      recipeRepository.findById(recipeId) ?: throw FNotFoundException(message = "Recipe not found!")

    val account = authorizationDetails.getSelf()

    val rating =
      ratingRepository.findByAccountAndRecipe(accountId = account.id, recipeId = recipe.id)
        ?: RatingEntity.create(account = account, recipe = recipe, rating = form.rating)

    rating.rating = form.rating

    ratingRepository.persist(rating)
  }

  fun deleteRecipesIdRating(recipeId: String) {
    val account = authorizationDetails.getSelf()

    ratingRepository.deleteByAccountAndRecipe(accountId = account.id, recipeId = recipeId)
  }
}
