/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.bring.controllers

import de.flavormate.extensions.bring.services.BringService
import de.flavormate.extensions.share.services.ShareService
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.enums.ImageResolution
import de.flavormate.utils.MimeTypes
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery

@Path("/v3/bring")
@RequestScoped
class BringController(val bringService: BringService, private val shareService: ShareService) {

  @POST
  @RolesAllowed(RoleTypes.USER_VALUE)
  @Path("/{recipeId}")
  fun getBringUrl(@PathParam("recipeId") recipeId: String) = bringService.getBringUrl(recipeId)

  @GET
  @Path("/{token}/{id}")
  @RolesAllowed(RoleTypes.BRING_VALUE)
  @Produces(MediaType.TEXT_HTML)
  fun shareBring(@RestPath token: String, @RestPath id: String) = bringService.shareBring(id = id)

  @GET
  @Path("/{token}/{id}/file")
  @RolesAllowed(RoleTypes.BRING_VALUE)
  @Produces(MimeTypes.WEBP_MIME)
  fun shareFile(
    @RestPath token: String,
    @RestPath id: String,
    @RestQuery resolution: ImageResolution?,
  ) = shareService.shareFile(id = id, resolution = resolution)
}
