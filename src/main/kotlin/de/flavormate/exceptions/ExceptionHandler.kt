/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.exceptions

import io.quarkus.logging.Log
import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.*
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class ExceptionHandler : ExceptionMapper<Throwable> {

  @Context private lateinit var uriInfo: UriInfo

  override fun toResponse(exception: Throwable): Response {
    val status: Response.Status =
      if (exception is WebApplicationException) {
        Response.Status.fromStatusCode(exception.response.status)
      } else {
        Response.Status.INTERNAL_SERVER_ERROR
      }

    val data =
      ExceptionDto(
        request = uriInfo.requestUri.toString(),
        statusCode = status.statusCode,
        statusText = status.name,
        message = "An error occurred",
      )
    val response =
      Response.status(status)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .entity(data)
        .build()

    Log.info("An error occurred: ", exception)

    return response
  }
}

@Provider
class ValidationExceptionHandler : ExceptionMapper<ConstraintViolationException> {

  @Context private lateinit var uriInfo: UriInfo

  override fun toResponse(exception: ConstraintViolationException): Response {
    val message =
      exception.constraintViolations.joinToString("\n") {
        "'${it.propertyPath.last().name}' ${it.message}"
      }

    val status = Response.Status.BAD_REQUEST

    val data =
      ExceptionDto(
        request = uriInfo.requestUri.toString(),
        statusCode = status.statusCode,
        statusText = status.name,
        message = message,
      )

    val response = Response.status(status).entity(data).build()

    return response
  }
}
