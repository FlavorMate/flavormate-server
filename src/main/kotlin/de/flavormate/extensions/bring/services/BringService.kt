/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.bring.services

import de.flavormate.core.auth.services.AuthTokenService
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.share.controllers.ShareController
import de.flavormate.extensions.urlShortener.services.ShortenerService
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.UriBuilder

@RequestScoped
class BringService(
  val authorizationDetails: AuthorizationDetails,
  val recipeRepository: RecipeRepository,
  val tokenService: AuthTokenService,
  val urlShortenerService: ShortenerService,
) {

  @Transactional
  fun getBringUrl(recipeId: String): String {
    val recipe =
      recipeRepository.findById(recipeId) ?: throw FNotFoundException(message = "Recipe not found")

    val token = tokenService.createAndSaveBringToken(authorizationDetails.getSelf(), recipe)

    val path =
      UriBuilder.fromResource(ShareController::class.java)
        .path(ShareController::class.java, ShareController::openRecipe.name)
        .queryParam("token", token)
        .build(recipe.id)
        .toString()

    return urlShortenerService.generateUrl(path)
  }
}
