/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.auth.oidc.controllers

import de.flavormate.core.auth.models.LoginForm
import de.flavormate.extensions.auth.oidc.services.OIDCServices
import io.quarkus.security.Authenticated
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.Response

@Path("/v3/oidc")
class OIDCController(private val service: OIDCServices) {

  @GET @Path("/") fun getProviders() = service.getProviders()

  @GET
  @Path("/icon/{id}")
  fun getIcon(@PathParam("id") id: String): Response {
    val stream = service.getIcon(id) ?: return Response.status(404).build()

    return Response.ok(stream)
      .header("Content-Disposition", "attachment; filename=\"provider_${id}.webp\"")
      .build()
  }

  @GET @Authenticated @Path("/login") fun login() = service.login()

  @POST @Authenticated @Path("/link") fun link(form: LoginForm) = service.link(form = form)
}
