/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.version.controller

import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.eclipse.microprofile.config.inject.ConfigProperty

@Path("/v3/version")
@RequestScoped
class VersionController {
  @ConfigProperty(name = "quarkus.application.version") private lateinit var version: String

  @Path("/") @GET fun getVersion(): String = version
}
