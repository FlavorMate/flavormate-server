/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.controllers

import de.flavormate.core.auth.models.LoginForm
import de.flavormate.extensions.auth.oidc.dto.models.OIDCExchangeForm
import de.flavormate.extensions.auth.oidc.dto.models.OIDCMappingDto
import de.flavormate.extensions.auth.oidc.services.OIDCServices
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import io.quarkus.security.Authenticated
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.UriInfo
import org.apache.hc.core5.http.message.BasicHeader
import org.apache.hc.core5.net.URIBuilder
import org.jboss.resteasy.reactive.RestPath

@Path("/v3/oidc2")
class OIDCController(private val service: OIDCServices) {

  @GET @Path("/") fun getProviders() = service.getProviders()

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

  @GET
  @Path("link")
  fun getLinks(@BeanParam pagination: Pagination): PageableDto<OIDCMappingDto> =
    service.getLinks(pagination = pagination)

  @DELETE
  @Path("link/{providerId}")
  fun deleteLink(@RestPath providerId: String): Boolean =
    service.deleteLink(providerId = providerId)
}
