/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.exceptions

import jakarta.ws.rs.core.Response
import java.io.Serializable

abstract class FException(
  val status: Response.Status,
  override val message: String,
  val id: String,
) : Exception(message), Serializable

class FBadRequestException(message: String, id: String = "") :
    FException(status = Response.Status.BAD_REQUEST, message = message, id = id)

class FConflictException(message: String, id: String = "") :
  FException(status = Response.Status.CONFLICT, message = message, id = id)

class FForbiddenException(message: String, id: String = "") :
  FException(status = Response.Status.FORBIDDEN, message = message, id = id)

class FNotFoundException(message: String, id: String = "") :
  FException(status = Response.Status.NOT_FOUND, message = message, id = id)

class FUnauthorizedException(message: String, id: String = "") :
  FException(status = Response.Status.UNAUTHORIZED, message = message, id = id)
