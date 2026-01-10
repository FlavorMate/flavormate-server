/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.categoryGroup.controllers

import de.flavormate.features.categoryGroup.services.CategoryGroupService
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.models.api.Pagination
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.validation.constraints.NotBlank
import jakarta.ws.rs.BeanParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.jboss.resteasy.reactive.RestQuery

@RequestScoped
@RolesAllowed(RoleTypes.USER_VALUE)
@Path("/v3/category-groups")
class CategoryGroupController(private val service: CategoryGroupService) {
  @GET
  fun getCategoryGroups(@RestQuery @NotBlank language: String, @BeanParam pagination: Pagination) =
    service.getCategoryGroups(language = language, pagination = pagination)
}
