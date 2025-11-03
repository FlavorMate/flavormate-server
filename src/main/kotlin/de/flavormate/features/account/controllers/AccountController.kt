/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.controllers

import de.flavormate.features.account.dtos.models.AccountFullDto
import de.flavormate.features.account.dtos.models.AccountUpdateDto
import de.flavormate.features.account.services.AccountService
import de.flavormate.features.account.services.file.AccountFileService
import de.flavormate.features.account.services.id.AccountIdService
import de.flavormate.shared.enums.ImageSquareResolution
import de.flavormate.shared.models.api.Pagination
import de.flavormate.utils.MimeTypes
import jakarta.enterprise.context.RequestScoped
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Response
import java.io.File
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery

@RequestScoped
@Path("/v3/accounts")
class AccountController(
  private val service: AccountService,
  private val accountFileService: AccountFileService,
  private val accountIdService: AccountIdService,
) {

  @GET fun getAccounts(@BeanParam pagination: Pagination) = service.getAccounts(pagination)

  @GET @Path("/self") fun getAccountsSelf(): AccountFullDto = service.getAccountsSelf()

  // Children
  @GET
  @Path("/search")
  fun getAccountsSearch(@RestQuery @NotBlank query: String, @BeanParam pagination: Pagination) =
    service.getAccountsSearch(query = query, pagination = pagination)

  // ID
  @GET
  @Path("/{id}")
  fun getAccountsId(@RestPath("id") @NotBlank id: String) = accountIdService.getAccountsId(id = id)

  @PUT
  @Path("/{id}")
  fun putAccountsId(@RestPath("id") @NotBlank id: String, @NotNull form: AccountUpdateDto) =
    accountIdService.putAccountsId(id = id, form = form)

  @GET
  @Path("/{id}/books")
  fun getAccountsIdBooks(@RestPath @NotBlank id: String, @BeanParam pagination: Pagination) =
    accountIdService.getAccountsIdBooks(id = id, pagination = pagination)

  @GET
  @Path("/{id}/recipes")
  fun getAccountsIdRecipes(@RestPath @NotBlank id: String, @BeanParam pagination: Pagination) =
    accountIdService.getAccountsIdRecipes(id = id, pagination = pagination)

  @GET
  @Path("/{id}/stories")
  fun getAccountsIdStories(@RestPath @NotBlank id: String, @BeanParam pagination: Pagination) =
    accountIdService.getAccountsIdStories(id = id, pagination = pagination)

  // ID - Avatar
  @GET
  @Produces(MimeTypes.WEBP_MIME)
  @Path("/{id}/avatar/{file}")
  fun getAccountsAvatar(
    @RestPath @NotBlank id: String,
    @RestQuery @NotNull resolution: ImageSquareResolution,
  ): Response {
    val stream = accountFileService.getAccountsIdFile(id = id, resolution = resolution)

    return Response.ok(stream)
      .header(
        "Content-Disposition",
        "attachment; filename=\"${id}_avatar_${resolution.name}.webp\"",
      )
      .build()
  }

  @POST
  @Path("/{id}/avatar")
  fun createAccountsIdAvatar(@RestForm @NotNull file: File, @RestPath @NotBlank id: String) =
    accountFileService.createAccountsIdAvatar(file = file, id = id)

  @DELETE
  @Path("/{id}/avatar")
  fun deleteAccountsIdAvatar(@RestPath @NotBlank id: String) =
    accountFileService.deleteAccountsIdAvatar(id = id)
}
