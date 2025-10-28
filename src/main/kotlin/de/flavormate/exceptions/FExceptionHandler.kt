/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.exceptions

import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.UriInfo
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
    val response = Response.status(exception.status).entity(data).build()

    return response
  }
}
