/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.services

import de.flavormate.core.auth.models.LoginForm
import de.flavormate.core.auth.models.TokenResponseDao
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.exceptions.FUnauthorizedException
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import io.quarkus.elytron.security.common.BcryptUtil
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class AuthService(
  private val accountRepository: AccountRepository,
  private val authorizationDetails: AuthorizationDetails,
  private val sessionService: AuthSessionService,
) {
  @Transactional
  fun login(loginForm: LoginForm): TokenResponseDao {
    val account =
      accountRepository.findByUsername(loginForm.username)
        ?: throw FNotFoundException(message = "Account with id ${loginForm.username} not found")

    if (!account.enabled)
      throw FUnauthorizedException(message = "Account not enabled by an administrator yet")
    if (!account.verified)
      throw FUnauthorizedException(message = "Account not validated. Please check your email")

    if (!BcryptUtil.matches(loginForm.password, account.password)) {
      throw FUnauthorizedException(message = "Invalid password")
    }

    return sessionService.login(account)
  }

  @Transactional
  fun loginOIDC(accountId: String): TokenResponseDao {
    val account =
      accountRepository.findById(accountId)
        ?: throw FNotFoundException(message = "Account with id $accountId not found")

    if (!account.enabled)
      throw FUnauthorizedException(message = "Account not enabled by an administrator yet")
    if (!account.verified)
      throw FUnauthorizedException(message = "Account not validated. Please check your email")

    return sessionService.login(account)
  }

  @Transactional fun logout() = sessionService.logout()

  @Transactional
  fun renewRefreshToken(): TokenResponseDao {
    val account = authorizationDetails.getSelf()

    val jwt = authorizationDetails.token
    // Generate new tokens
    return sessionService.refreshToken(jwt, account)
  }

  fun getAllSessions(pagination: Pagination) = sessionService.getAllSessions(pagination)

  @Transactional fun deleteSession(id: String) = sessionService.deleteSession(id = id)
}
