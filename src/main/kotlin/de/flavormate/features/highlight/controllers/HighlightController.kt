/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.highlight.controllers

import de.flavormate.features.highlight.services.HighlightService
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.models.api.Pagination
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.BeanParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.jboss.resteasy.reactive.RestQuery

@Path("/v3/highlights")
@RolesAllowed(RoleTypes.USER_VALUE)
@RequestScoped
class HighlightController(private val service: HighlightService) {

  @GET
  fun getHighlights(@RestQuery @NotNull diet: Diet, @BeanParam pagination: Pagination) =
    service.getHighlights(diet = diet, pagination = pagination)
}
