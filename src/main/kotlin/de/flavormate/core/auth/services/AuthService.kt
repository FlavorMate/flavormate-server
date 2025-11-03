/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.auth.services

import de.flavormate.core.auth.models.LoginForm
import de.flavormate.core.auth.models.TokenResponseDao
import de.flavormate.exceptions.FBadRequestException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.exceptions.FUnauthorizedException
import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.account.repositories.AccountRepository
import io.quarkus.elytron.security.common.BcryptUtil
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class AuthService(val accountRepository: AccountRepository, val tokenService: AuthTokenService) {
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

    return tokenService.createTokenPair(account)
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

    return tokenService.createTokenPair(account)
  }

  @Transactional
  fun renewRefreshToken(account: AccountEntity, oldJWT: String): TokenResponseDao {
    val isStillValid = tokenService.isValidToken(oldJWT, account.id)

    if (!isStillValid) throw FBadRequestException(message = "Invalid refresh token")

    // Revoke old refresh token
    tokenService.revokeJWT(oldJWT)

    // Generate new tokens
    return tokenService.createTokenPair(account)
  }
}
