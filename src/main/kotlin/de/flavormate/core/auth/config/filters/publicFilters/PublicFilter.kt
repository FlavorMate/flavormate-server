/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.config.filters.publicFilters

import de.flavormate.core.auth.services.AuthTokenService
import de.flavormate.exceptions.FUnauthorizedException
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ResourceInfo
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.UriInfo
import java.lang.reflect.Method
import org.jboss.resteasy.reactive.server.jaxrs.ContainerRequestContextImpl

/**
 * Abstract base class for implementing HTTP request filters that perform token-based authorization
 * for specific classes and methods. This filter is designed to ensure that only authenticated and
 * authorized requests are allowed to access secured endpoints.
 *
 * Subclasses must define the secured class and its associated secured methods that require
 * filtering.
 *
 * @param tokenService The service used to validate the access token and associated ID within the
 *   filter logic.
 * @constructor Initializes the filter with an instance of [AuthTokenService], which is responsible
 *   for token validation.
 */
abstract class PublicFilter(private val tokenService: AuthTokenService) : ContainerRequestFilter {

  @Context private lateinit var resourceInfo: ResourceInfo

  @Context private lateinit var uriInfo: UriInfo

  protected abstract val securedClass: Class<*>
  protected abstract val securedMethods: List<Method?>

  /**
   * Filters incoming HTTP requests by verifying tokens and IDs for specific secured resources and
   * methods. This method ensures that only authorized requests are processed further.
   *
   * @param requestContext The context of the incoming HTTP request, which may contain headers,
   *   parameters, and other metadata about the request. If `null` or not an instance of
   *   `ContainerRequestContextImpl`, the method exits early.
   */
  override fun filter(requestContext: ContainerRequestContext?) {
    if (requestContext !is ContainerRequestContextImpl) return

    val resourceMethod = resourceInfo.resourceMethod
    val resourceClass = resourceInfo.resourceClass

    if (securedClass != resourceClass) return
    if (!securedMethods.any { it == resourceMethod }) return

    val token =
      uriInfo.pathParameters.getFirst("token")
        ?: throw FUnauthorizedException("Token parameter missing")
    val id =
      uriInfo.pathParameters.getFirst("id") ?: throw FUnauthorizedException("Id parameter missing")

    val isValid = tokenService.validateAccess(token, id)

    if (!isValid) {
      throw FUnauthorizedException("Token is invalid")
    }
  }
}
