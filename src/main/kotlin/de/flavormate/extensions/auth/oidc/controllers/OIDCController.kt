/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.controllers

import de.flavormate.core.auth.models.LoginForm
import de.flavormate.extensions.auth.oidc.dto.models.OIDCExchangeForm
import de.flavormate.extensions.auth.oidc.services.OIDCServices
import io.quarkus.security.Authenticated
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.UriInfo
import org.apache.hc.core5.http.message.BasicHeader
import org.apache.hc.core5.net.URIBuilder

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

  @POST
  @Path("/exchange-code")
  fun exchangeToken(form: OIDCExchangeForm) = service.exchangeToken(form = form)

  @GET
  @Path("/mobile-redirect")
  fun mobileRedirect(@Context uriInfo: UriInfo): Response {
    val uriBuilder = URIBuilder("flavormate://oauth")

    val queryParameters =
      uriInfo.queryParameters.flatMap { (key, values) ->
        values.map { value -> BasicHeader(key, value) }
      }

    if (queryParameters.isNotEmpty()) {
      uriBuilder.addParameters(queryParameters)
    }

    return Response.temporaryRedirect(uriBuilder.build()).build()
  }
}
