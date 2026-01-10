/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.ratings.controllers

import de.flavormate.extensions.ratings.dtos.RecipeRatingFormDto
import de.flavormate.extensions.ratings.services.RatingsService
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import org.jboss.resteasy.reactive.RestPath

@RequestScoped
@Path("/v3/ratings")
class RatingsController(private val service: RatingsService) {

  @GET
  @Path("/{recipeId}")
  fun getRecipesIdRating(@RestPath recipeId: String) =
    service.getRecipesIdRating(recipeId = recipeId)

  @PUT
  @Path("/{recipeId}")
  fun putRecipesIdRating(@RestPath recipeId: String, form: RecipeRatingFormDto) =
    service.putRecipesIdRating(recipeId = recipeId, form = form)

  @DELETE
  @Path("/{recipeId}")
  fun deleteRecipesIdRating(@RestPath recipeId: String) =
    service.deleteRecipesIdRating(recipeId = recipeId)
}
