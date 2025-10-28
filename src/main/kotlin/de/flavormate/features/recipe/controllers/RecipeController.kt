/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.controllers

import de.flavormate.features.recipe.dtos.models.RecipeTransferDto
import de.flavormate.features.recipe.services.RecipeService
import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.enums.ImageWideResolution
import de.flavormate.shared.models.api.Pagination
import de.flavormate.utils.MimeTypes
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.*
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery

@RequestScoped
@Path("/v3/recipes")
class RecipeController(private val service: RecipeService) {

  @GET
  fun getRecipes(@BeanParam pagination: Pagination) = service.getRecipes(pagination = pagination)

  @GET
  @Path("/random")
  fun getRecipesRandom(
    @RestQuery diet: Diet,
    @RestQuery course: Course?,
    @BeanParam pagination: Pagination,
  ) = service.getRecipesRandom(diet = diet, course = course, pagination = pagination)

  @GET
  @Path("/search")
  fun getRecipesSearch(@RestQuery query: String, @BeanParam pagination: Pagination) =
    service.getRecipesSearch(query = query, pagination = pagination)

  @GET
  @Path("/{id}")
  fun getRecipe(@RestPath id: String, @RestQuery language: String) =
    service.getRecipe(id = id, language = language)

  @POST @Path("/{id}") fun postRecipesId(@RestPath id: String) = service.postRecipesId(id = id)

  @DELETE
  @Path("/{id}")
  fun deleteRecipesId(@RestPath id: String) = service.deleteRecipesId(id = id)

  @GET
  @Path("/{recipeId}/book/{bookId}")
  fun getRecipesIdIsInBook(@RestPath recipeId: String, @RestPath bookId: String) =
    service.getRecipesIdIsInBook(recipeId = recipeId, bookId = bookId)

  @GET
  @Produces(MimeTypes.WEBP_MIME)
  @Path("/{id}/cover")
  fun getRecipeCover(@RestPath id: String, @RestQuery resolution: ImageWideResolution) =
    service.streamCover(id = id, resolution = resolution)

  @GET
  @Path("/{id}/files")
  fun getRecipesFiles(@RestPath id: String, @BeanParam pagination: Pagination) =
    service.getRecipesFiles(id = id, pagination = pagination)

  @GET
  @Produces(MimeTypes.WEBP_MIME)
  @Path("/{id}/files/{file}")
  fun getRecipesFilesId(
    @RestPath id: String,
    @RestPath file: String,
    @RestQuery resolution: ImageWideResolution,
  ) = service.streamFile(id = id, file = file, resolution = resolution)

  @PUT
  @Path("/{id}/transfer")
  fun putRecipesIdTransfer(@RestPath id: String, form: RecipeTransferDto) =
    service.putRecipesIdTransfer(id = id, form = form)
}
