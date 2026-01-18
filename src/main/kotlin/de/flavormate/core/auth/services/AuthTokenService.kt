/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.services

import de.flavormate.configuration.properties.auth.jwt.JWTProperties
import de.flavormate.core.auth.config.JwtBlockList
import de.flavormate.core.auth.models.TokenResponseDao
import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.token.daos.TokenEntity
import de.flavormate.features.token.enums.TokenType
import de.flavormate.features.token.repositories.TokenRepository
import de.flavormate.shared.extensions.mapToSet
import io.smallrye.jwt.build.Jwt
import jakarta.enterprise.context.ApplicationScoped
import java.security.MessageDigest
import java.time.Duration
import java.time.Instant

@ApplicationScoped
class AuthTokenService(
  val tokenRepository: TokenRepository,
  val jwtBlockList: JwtBlockList,
  val jwtProperties: JWTProperties,
) {

  companion object {
    fun hashJWT(token: String): String {
      return MessageDigest.getInstance("SHA-256").digest(token.toByteArray()).joinToString("") {
        "%02x".format(it)
      }
    }
  }

  // Token creation functions
  fun createTokenPair(account: AccountEntity): TokenResponseDao {
    val accessToken = createAccessToken(account)
    val refreshToken = createAndSaveRefreshToken(account)

    return TokenResponseDao.create(
      accessToken,
      jwtProperties.accessToken().duration().toSeconds(),
      refreshToken,
    )
  }

  fun createAccessToken(account: AccountEntity): String {
    val roles = account.roles.map { it.role }.toSet()

    return generateToken(account.id, roles, jwtProperties.accessToken().duration())
  }

  fun createAndSaveRefreshToken(account: AccountEntity): String {
    val roles = setOf(RoleTypes.Refresh)

    val refreshToken = generateToken(account.id, roles, jwtProperties.refreshToken().duration())

    saveToken(
      refreshToken,
      TokenType.ACCOUNT,
      account.id,
      account,
      jwtProperties.refreshToken().duration(),
    )

    return refreshToken
  }

  fun createAndSaveResetToken(account: AccountEntity): String {
    val roles = setOf(RoleTypes.Reset)

    val jwt = generateToken(account.id, roles, jwtProperties.resetToken().duration())

    saveToken(jwt, TokenType.RESET, account.id, account, jwtProperties.resetToken().duration())

    return jwt
  }

  fun createAndSaveVerifyToken(account: AccountEntity): String {
    val roles = setOf(RoleTypes.Verify)

    val jwt = generateToken(account.id, roles, jwtProperties.verifyToken().duration())

    saveToken(jwt, TokenType.VERIFY, account.id, account, jwtProperties.verifyToken().duration())

    return jwt
  }

  fun createAndSaveBringToken(account: AccountEntity, recipe: RecipeEntity): String {
    val roles = setOf(RoleTypes.Bring)

    val jwt = generateToken(account.id, roles, jwtProperties.bringToken().duration())

    saveToken(jwt, TokenType.BRING, recipe.id, account, jwtProperties.bringToken().duration())

    return jwt
  }

  fun createAndSaveShareToken(account: AccountEntity, recipe: RecipeEntity): String {
    val roles = setOf(RoleTypes.Share)

    val jwt = generateToken(account.id, roles, jwtProperties.shareToken().duration())

    saveToken(jwt, TokenType.SHARE, recipe.id, account, jwtProperties.shareToken().duration())

    return jwt
  }

  // Token renewal functions
  fun isValidToken(jwt: String, securedResource: String): Boolean {
    val hashedJWT = hashJWT(jwt)
    return tokenRepository.verifyRefreshToken(hashedJWT, securedResource)
  }

  fun revokeJWT(jwt: String): Boolean {
    jwtBlockList.add(jwt)
    val hashedJWT = hashJWT(jwt)
    return tokenRepository.revokeToken(hashedJWT)
  }

  // General jwt functions

  fun validateAccess(token: String, id: String): Boolean {
    val tokenEntity = tokenRepository.findByToken(hashJWT(token)) ?: return false

    return tokenEntity.securedResource == id
  }

  private fun generateToken(accountId: String, roles: Set<RoleTypes>, duration: Duration?): String {
    val tokenBuilder =
      Jwt.issuer(jwtProperties.issuer())
        .subject(accountId)
        .groups(roles.mapToSet { it.name })
        .expiresAt(Instant.MAX)

    duration?.let { Instant.now().plus(it) }?.also { tokenBuilder.expiresAt(it) }

    return tokenBuilder.sign()
  }

  private fun saveToken(
    jwt: String,
    type: TokenType,
    securedResource: String,
    issuer: AccountEntity?,
    duration: Duration?,
  ): TokenEntity {
    val hashedJWT = hashJWT(jwt)
    return TokenEntity.create(hashedJWT, type, securedResource, issuer, duration).also {
      tokenRepository.persist(it)
    }
  }
}
