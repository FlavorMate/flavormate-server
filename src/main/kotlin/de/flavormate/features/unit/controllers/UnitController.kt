/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.unit.controllers

import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.unit.services.UnitService
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
@Path("/v3/units")
class UnitController(private val service: UnitService) {

  @GET
  fun getUnitsByLanguage(@RestQuery @NotBlank language: String, @BeanParam pagination: Pagination) =
    service.getUnitsByLanguage(language = language, pagination = pagination)
}
