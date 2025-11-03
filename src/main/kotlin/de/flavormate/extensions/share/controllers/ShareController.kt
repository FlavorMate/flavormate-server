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

/**
 * Bring -> share/{token}/bring
 *
 * In App -> share/{token}/in-app
 *
 * Web -> share/{token}/web
 *
 * File -> share/{token}/file
 */
@RequestScoped
@Path("/v3/share")
class ShareController(val service: ShareService) {

  @Path("/{id}")
  @RolesAllowed(RoleTypes.USER_VALUE)
  @POST
  fun createShareLink(@PathParam("id") id: String) = service.createShareLink(id)

  @Path("/{token}/{id}/web")
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed(RoleTypes.SHARE_VALUE)
  @GET
  fun shareWeb(@RestPath token: String, @RestPath id: String) = service.shareWeb(id)

  @GET
  @Path("/{token}/{id}/file")
  @RolesAllowed(RoleTypes.SHARE_VALUE)
  @Produces(MimeTypes.WEBP_MIME)
  fun shareFile(
    @RestPath token: String,
    @RestPath id: String,
    @RestQuery resolution: ImageWideResolution?,
  ) = service.shareFile(id = id, resolution = resolution)

  @Path("/{token}/{id}/in-app")
  @RolesAllowed(RoleTypes.SHARE_VALUE)
  @GET
  fun openInApp(@RestPath token: String, @RestPath id: String, @RestQuery language: String) =
    service.openInApp(id = id, language = language)
}
