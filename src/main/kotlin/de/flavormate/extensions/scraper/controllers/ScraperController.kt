/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.scraper.controllers

import de.flavormate.extensions.scraper.services.ScraperService
import de.flavormate.features.role.enums.RoleTypes
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import java.net.URLDecoder
import java.util.*

@RequestScoped
@Path("/v3/scraper")
@RolesAllowed(RoleTypes.USER_VALUE)
class ScraperController(val service: ScraperService) {

  @GET
  @Path("/{base64}")
  fun scrape(base64: String): String {
    val uri = Base64.getDecoder().decode(base64).decodeToString()
    val url = URLDecoder.decode(uri, Charsets.UTF_8)

    return service.scrape(url = url)
  }
}
