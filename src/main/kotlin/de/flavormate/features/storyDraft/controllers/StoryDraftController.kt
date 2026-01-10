/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.storyDraft.controllers

import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.storyDraft.dtos.models.StoryDraftUpdateDto
import de.flavormate.features.storyDraft.services.StoryDraftService
import de.flavormate.shared.models.api.Pagination
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.*
import org.jboss.resteasy.reactive.RestPath

@RequestScoped
@RolesAllowed(RoleTypes.USER_VALUE)
@Path("/v3/story-drafts")
class StoryDraftController(private val service: StoryDraftService) {

  @GET
  fun getStoryDrafts(@BeanParam pagination: Pagination) =
    service.getStoryDrafts(pagination = pagination)

  @POST fun postStoryDrafts() = service.postStoryDrafts()

  // ID
  @GET
  @Path("/{id}")
  fun getStoryDraftsId(@RestPath @NotBlank id: String) = service.getStoryDraftsId(id = id)

  @DELETE
  @Path("/{id}")
  fun deleteStoryDraftsId(@RestPath @NotBlank id: String) = service.deleteStoryDraftsId(id = id)

  @PUT
  @Path("/{id}")
  fun putStoryDraftsId(@RestPath @NotBlank id: String, @NotNull form: StoryDraftUpdateDto) =
    service.putStoryDraftsId(id = id, form = form)

  @POST
  @Path("/{id}")
  fun postStoryDraftsId(@RestPath @NotBlank id: String) = service.postStoryDraftsId(id = id)
}
