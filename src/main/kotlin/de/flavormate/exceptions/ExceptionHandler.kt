/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.exceptions

import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.UriInfo
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class ExceptionHandler : ExceptionMapper<Throwable> {

  @Context private lateinit var uriInfo: UriInfo

  override fun toResponse(exception: Throwable): Response {
    val status = Response.Status.INTERNAL_SERVER_ERROR

    val data =
      ExceptionDto(
        request = uriInfo.requestUri.toString(),
        statusCode = status.statusCode,
        statusText = status.name,
        message = "An unknown error occurred, please try again later",
      )
    val response = Response.status(status).entity(data).build()

    exception.printStackTrace()

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
