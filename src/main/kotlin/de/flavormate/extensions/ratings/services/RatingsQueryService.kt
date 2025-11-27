/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.ratings.services

import de.flavormate.extensions.ratings.dtos.RatingsDto
import de.flavormate.extensions.ratings.repositories.RatingsRepository
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class RatingsQueryService(
  private val authorizationDetails: AuthorizationDetails,
  private val repository: RatingsRepository,
) {

  fun getRecipesIdRating(recipeId: String): RatingsDto? {
    val self = authorizationDetails.getSelf()
    return repository.findByRecipeId(accountId = self.id, recipeId = recipeId)
  }
}
