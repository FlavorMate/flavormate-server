/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.admin.controllers

import de.flavormate.features.admin.dto.AccountForm
import de.flavormate.features.admin.services.AdminService
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.models.api.Pagination
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.*
import org.jboss.resteasy.reactive.RestPath

@RequestScoped
@RolesAllowed(RoleTypes.ADMIN_VALUE)
@Path("/v3/admin")
class AdminController(private val service: AdminService) {
  @GET
  @Path("/accounts")
  fun getAdminAccounts(@BeanParam pagination: Pagination) =
    service.getAdminAccounts(pagination = pagination)

  @POST
  @Path("/account")
  fun createAccount(@NotNull form: AccountForm) = service.createAccount(form = form)

  @DELETE
  @Path("/account/{id}")
  fun deleteAccount(@RestPath id: String) = service.deleteAccount(id = id)

  @PUT
  @Path("/account/{id}")
  fun setAccountPassword(@RestPath id: String, @NotBlank form: String) =
    service.setPassword(id = id, newPassword = form)

  @PUT
  @Path("/account/{id}/activeState")
  fun putActiveState(@RestPath("id") @NotBlank id: String) = service.toggleActiveState(id = id)
}
