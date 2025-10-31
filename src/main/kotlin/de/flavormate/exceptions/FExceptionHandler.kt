/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.exceptions

import jakarta.ws.rs.core.*
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class FExceptionHandler : ExceptionMapper<FException> {

  @Context private lateinit var uriInfo: UriInfo

  override fun toResponse(exception: FException): Response {
    val data =
      ExceptionDto(
        request = uriInfo.requestUri.toString(),
        statusCode = exception.status.statusCode,
        statusText = exception.status.name,
        message = exception.message,
        id = exception.id,
      )
      val response =
          Response.status(exception.status).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).entity(data)
              .build()

    return response
  }
}
