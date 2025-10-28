/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.bring.controllers

import de.flavormate.extensions.bring.services.BringService
import de.flavormate.features.role.enums.RoleTypes
import jakarta.annotation.security.RolesAllowed
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam

@Path("/v3/bring")
@RolesAllowed(RoleTypes.USER_VALUE)
class BringController(val bringService: BringService) {

  @POST
  @Path("/{recipeId}")
  fun getBringUrl(@PathParam("recipeId") recipeId: String) = bringService.getBringUrl(recipeId)
}
