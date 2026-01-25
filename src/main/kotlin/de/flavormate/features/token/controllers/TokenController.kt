/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.token.controllers

import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.token.services.TokenService
import de.flavormate.shared.models.api.Pagination
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.BeanParam
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path

@RequestScoped
@RolesAllowed(RoleTypes.USER_VALUE)
@Path("/v3/tokens")
class TokenController(private val tokenService: TokenService) {

  @GET
  @Path("/")
  fun getAllTokens(@BeanParam pagination: Pagination) = tokenService.getAllTokens(pagination)

  @DELETE @Path("/{id}") fun deleteToken(id: String) = tokenService.deleteToken(id = id)
}
