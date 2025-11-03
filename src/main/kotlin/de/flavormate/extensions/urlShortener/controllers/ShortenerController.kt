/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.urlShortener.controllers

import de.flavormate.extensions.urlShortener.services.ShortenerService
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.Response
import java.net.URI

@RequestScoped
@Path("/s")
class ShortenerController(val shortenerService: ShortenerService) {

  @GET
  @Path("/{shortPath}")
  fun redirectToOriginalUrl(@PathParam("shortPath") shortPath: String): Response {
    val newLocation = shortenerService.findByShortPath(shortPath)

    return Response.status(Response.Status.PERMANENT_REDIRECT)
      .location(URI.create(newLocation))
      .build()
  }
}
