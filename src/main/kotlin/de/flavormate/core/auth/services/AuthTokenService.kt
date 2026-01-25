/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.services

import de.flavormate.configuration.properties.auth.jwt.JWTProperties
import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.token.daos.TokenEntity
import de.flavormate.features.token.enums.TokenType
import de.flavormate.features.token.repositories.TokenRepository
import de.flavormate.utils.JwtUtils
import jakarta.enterprise.context.ApplicationScoped
import java.time.Duration

@ApplicationScoped
class AuthTokenService(val tokenRepository: TokenRepository, val jwtProperties: JWTProperties) {
  fun createAndSaveResetToken(account: AccountEntity): String {
    val roles = setOf(RoleTypes.Reset)

    val jwt =
      JwtUtils.generateToken(
        jwtProperties.issuer(),
        account.id,
        roles,
        jwtProperties.resetToken().duration(),
      )

    saveToken(jwt, TokenType.RESET, account.id, account, jwtProperties.resetToken().duration())

    return jwt
  }

  fun createAndSaveVerifyToken(account: AccountEntity): String {
    val roles = setOf(RoleTypes.Verify)

    val jwt =
      JwtUtils.generateToken(
        jwtProperties.issuer(),
        account.id,
        roles,
        jwtProperties.verifyToken().duration(),
      )

    saveToken(jwt, TokenType.VERIFY, account.id, account, jwtProperties.verifyToken().duration())

    return jwt
  }

  fun createAndSaveBringToken(account: AccountEntity, recipe: RecipeEntity): String {
    val roles = setOf(RoleTypes.Bring)

    val jwt =
      JwtUtils.generateToken(
        jwtProperties.issuer(),
        account.id,
        roles,
        jwtProperties.bringToken().duration(),
      )

    saveToken(jwt, TokenType.BRING, recipe.id, account, jwtProperties.bringToken().duration())

    return jwt
  }

  fun createAndSaveShareToken(account: AccountEntity, recipe: RecipeEntity): String {
    val roles = setOf(RoleTypes.Share)

    val jwt =
      JwtUtils.generateToken(
        jwtProperties.issuer(),
        account.id,
        roles,
        jwtProperties.shareToken().duration(),
      )

    saveToken(jwt, TokenType.SHARE, recipe.id, account, jwtProperties.shareToken().duration())

    return jwt
  }

  // Token renewal functions
  fun revokeJWT(jwt: String): Boolean {
    val hashedJWT = JwtUtils.hashJWT(jwt)
    return tokenRepository.revokeToken(hashedJWT)
  }

  // General jwt functions

  fun validateAccess(token: String, id: String): Boolean {
    val tokenEntity = tokenRepository.findByToken(JwtUtils.hashJWT(token)) ?: return false

    return tokenEntity.securedResource == id
  }

  private fun saveToken(
    jwt: String,
    type: TokenType,
    securedResource: String,
    issuer: AccountEntity?,
    duration: Duration?,
  ): TokenEntity {
    val hashedJWT = JwtUtils.hashJWT(jwt)
    return TokenEntity.create(hashedJWT, type, securedResource, issuer, duration).also {
      tokenRepository.persist(it)
    }
  }

  fun isRevoked(token: String): Boolean {
    val hash = JwtUtils.hashJWT(token)
    val jwt = tokenRepository.findByToken(hash) ?: return true
    return jwt.revoked
  }
}
