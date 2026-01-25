/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.controllers

import de.flavormate.core.auth.models.LoginForm
import de.flavormate.core.auth.models.TokenResponseDao
import de.flavormate.core.auth.services.AuthService
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.models.api.Pagination
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.*

/**
 * Controller class responsible for handling authentication-related HTTP requests.
 *
 * This class provides endpoints for user login and token refresh functionality. It interacts with
 * the `AuthService` to perform these operations and uses `AuthorizationDetails` to retrieve or
 * validate the caller's authentication details.
 *
 * @property authService The service responsible for managing authentication operations.
 * @property authorizationDetails The details of the caller's authorization, such as tokens and
 *   roles.
 * @constructor Creates an instance of `AuthController` with the specified dependencies.
 */
@Path("/v3/auth")
@RequestScoped
class AuthController(val authService: AuthService) {

  /**
   * Authenticates a user based on the provided login details and generates a token pair.
   *
   * This method takes a `LoginForm` containing the user's username and password, validates the
   * credentials, and returns a `TokenResponseDao` which includes access and refresh tokens as well
   * as their associated metadata.
   *
   * @param loginForm The login form containing the username and password for authentication.
   * @return A `TokenResponseDao` containing the generated access token, refresh token, token type,
   *   and expiration duration.
   */
  @Path("/login")
  @POST
  fun login(loginForm: LoginForm): TokenResponseDao = authService.login(loginForm)

  /**
   * Refreshes the authentication tokens by generating a new pair of access token and refresh token.
   *
   * This method validates the caller's current refresh token, revokes it if valid, and issues a new
   * token pair. It ensures the caller retains their authenticated state.
   *
   * @return A `TokenResponseDao` containing the newly generated access token, refresh token, token
   *   type, and expiration duration.
   */
  @Path("/refresh")
  @RolesAllowed(RoleTypes.REFRESH_VALUE)
  @POST
  fun refresh(): TokenResponseDao = authService.renewRefreshToken()

  @RolesAllowed(RoleTypes.REFRESH_VALUE) @Path("/logout") @POST fun logout() = authService.logout()

  @RolesAllowed(RoleTypes.REFRESH_VALUE)
  @Path("/logout/all")
  @POST
  fun logoutAll() = authService.logoutAll()

  @RolesAllowed(RoleTypes.USER_VALUE)
  @Path("/sessions")
  @GET
  fun getAllSessions(@BeanParam pagination: Pagination) =
    authService.getAllSessions(pagination = pagination)

  @RolesAllowed(RoleTypes.USER_VALUE)
  @Path("/sessions/{id}")
  @DELETE
  fun deleteSession(id: String) = authService.deleteSession(id = id)
}
