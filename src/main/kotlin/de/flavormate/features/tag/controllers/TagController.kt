/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.tag.controllers

import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.tag.services.TagService
import de.flavormate.shared.models.api.Pagination
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.validation.constraints.NotBlank
import jakarta.ws.rs.BeanParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery

@RequestScoped
@RolesAllowed(RoleTypes.USER_VALUE)
@Path("/v3/tags")
class TagController(private val service: TagService) {

  @GET fun getTags(@BeanParam pagination: Pagination) = service.getTags(pagination = pagination)

  @GET
  @Path("/search")
  fun getTagsSearch(@RestQuery @NotBlank query: String, @BeanParam pagination: Pagination) =
    service.getTagsSearch(query = query, pagination = pagination)

  // ID

  @GET @Path("/{id}") fun getTag(@RestPath @NotBlank id: String) = service.getTag(id = id)

  @GET
  @Path("/{id}/recipes")
  fun getTagRecipes(@RestPath @NotBlank id: String, @BeanParam pagination: Pagination) =
    service.getTagRecipes(id = id, pagination = pagination)
}
