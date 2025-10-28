/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.share.controllers

import de.flavormate.extensions.share.services.ShareService
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.enums.ImageWideResolution
import de.flavormate.utils.MimeTypes
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery

@RequestScoped
@Path("/v3/share")
class ShareController(val service: ShareService) {

  @Path("/{id}")
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed(RoleTypes.BRING_VALUE, RoleTypes.SHARE_VALUE)
  @GET
  fun openRecipe(@PathParam("id") id: String) = service.openRecipe(id)

  @Path("/{id}/raw")
  @RolesAllowed(RoleTypes.BRING_VALUE, RoleTypes.SHARE_VALUE)
  @GET
  fun openRecipeRaw(@RestPath id: String, @RestQuery language: String) =
    service.openRecipeRaw(id = id, language = language)

  @Path("/{id}/cover")
  @RolesAllowed(RoleTypes.BRING_VALUE, RoleTypes.SHARE_VALUE)
  @Produces(MimeTypes.WEBP_MIME)
  @GET
  fun openRecipeFile(@RestPath id: String, @RestQuery resolution: ImageWideResolution) =
    service.openRecipeFile(id = id, resolution = resolution)

  @Path("/{id}")
  @RolesAllowed(RoleTypes.USER_VALUE)
  @POST
  fun createShareLink(@PathParam("id") id: String) = service.createShareLink(id)
}
