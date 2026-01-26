/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.config.filters.validationFilters

import de.flavormate.core.auth.services.AuthTokenService
import de.flavormate.exceptions.FUnauthorizedException
import de.flavormate.extensions.bring.controllers.BringController
import de.flavormate.extensions.recovery.controllers.RecoveryController
import de.flavormate.extensions.registration.controllers.RegistrationController
import de.flavormate.extensions.share.controllers.ShareController
import jakarta.annotation.Priority
import jakarta.inject.Singleton
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ResourceInfo
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.UriInfo
import jakarta.ws.rs.ext.Provider
import kotlin.reflect.jvm.javaMethod

@Provider
@Singleton
@Priority(150)
class ValidationFilter(private val tokenService: AuthTokenService) : ContainerRequestFilter {

  private val securedClass by lazy {
    listOf(
      BringController::class.java,
      RecoveryController::class.java,
      RegistrationController::class.java,
      ShareController::class.java,
    )
  }
  private val securedMethods by lazy {
    listOf(
      BringController::shareBring.javaMethod,
      BringController::shareFile.javaMethod,
      RecoveryController::showPasswordResetPage.javaMethod,
      RecoveryController::handlePasswordReset.javaMethod,
      RegistrationController::register.javaMethod,
      ShareController::shareWeb.javaMethod,
      ShareController::shareFile.javaMethod,
      ShareController::openInApp.javaMethod,
    )
  }

  @Context private lateinit var resourceInfo: ResourceInfo

  @Context private lateinit var uriInfo: UriInfo

  override fun filter(requestContext: ContainerRequestContext?) {
    val resourceMethod = resourceInfo.resourceMethod
    val resourceClass = resourceInfo.resourceClass

    if (!securedClass.any { it == resourceClass }) return
    if (!securedMethods.any { it == resourceMethod }) return

    val token =
      uriInfo.pathParameters.getFirst("token")
        ?: throw FUnauthorizedException("Id parameter missing")

    val isRevoked = tokenService.isRevoked(token)

    if (isRevoked) throw FUnauthorizedException("Token is revoked")
  }
}
