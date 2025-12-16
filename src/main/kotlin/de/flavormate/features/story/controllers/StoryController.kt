/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.story.controllers

import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.story.services.StoryService
import de.flavormate.shared.models.api.Pagination
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.validation.constraints.NotBlank
import jakarta.ws.rs.*
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery

@RequestScoped
@RolesAllowed(RoleTypes.USER_VALUE)
@Path("/v3/stories")
class StoryController(private val service: StoryService) {

  // GET
  @GET
  fun getStories(@BeanParam pagination: Pagination) = service.getStories(pagination = pagination)

  @GET @Path("/{id}") fun getStoriesId(@RestPath id: String) = service.getStoriesId(id = id)

  @GET
  @Path("/search")
  fun getStoriesSearch(@RestQuery query: String, @BeanParam pagination: Pagination) =
    service.getStoriesSearch(query = query, pagination = pagination)

  // POST
  @POST
  @Path("/{id}")
  fun editStoriesId(@RestPath @NotBlank id: String) = service.editStoriesId(id = id)

  // DELETE
  @DELETE
  @Path("/{id}")
  fun deleteStoriesId(@RestPath id: String) = service.deleteStoriesId(id = id)
}
