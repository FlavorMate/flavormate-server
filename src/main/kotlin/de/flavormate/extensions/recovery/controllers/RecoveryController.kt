/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.recovery.controllers

import de.flavormate.extensions.recovery.services.RecoveryService
import de.flavormate.features.role.enums.RoleTypes
import io.quarkus.arc.lookup.LookupIfProperty
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestForm

/**
 * Controller responsible for handling user account recovery operations.
 *
 * This class provides endpoints to initiate password reset processes, render password reset pages,
 * and handle password reset requests. The operations are secured to ensure that only authorized
 * users can access specific resources.
 *
 * @property service The recovery service used to handle business logic for password recovery and
 *   related processes.
 */
@LookupIfProperty(name = "flavormate.features.recovery.enabled", stringValue = "true")
@RequestScoped
@Path("/v3/recovery")
class RecoveryController(val service: RecoveryService) {
  /**
   * Initiates the password reset process for the specified email address.
   *
   * This endpoint triggers the password reset workflow by verifying the account associated with the
   * given email, generating a reset token, creating a shortened URL for the reset link, and sending
   * a password recovery email to the user.
   *
   * @param email The email address of the user requesting the password reset. Must correspond to an
   *   existing account in the system.
   */
  @PUT
  @Path("/password/reset")
  fun requestPasswordReset(email: String) = service.requestPasswordReset(email)

  /**
   * Displays the password reset page.
   *
   * This method is responsible for rendering the password reset page, providing the necessary user
   * interface to initiate and process the password reset.
   *
   * The operation requires the caller to have the "Reset" role permission, as defined in
   * `RoleTypes.Companion.RESET_VALUE`.
   *
   * @return A rendered HTML response representing the password reset page.
   */
  @GET
  @RolesAllowed(RoleTypes.RESET_VALUE)
  @Produces(MediaType.TEXT_HTML)
  @Path("/password/reset")
  fun showPasswordResetPage() = service.showPasswordResetPage()

  /**
   * Handles the password reset process by updating the user's password.
   *
   * This method validates the reset token, securely updates the user's password, and renders the
   * appropriate response template indicating the result of the operation.
   *
   * @param password The new password provided by the user for the password reset process.
   */
  @POST
  @RolesAllowed(RoleTypes.RESET_VALUE)
  @Produces(MediaType.TEXT_HTML)
  @Path("/password/reset")
  fun handlePasswordReset(@RestForm password: String) = service.handlePasswordReset(password)
}
