/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.token.repositories

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.token.daos.TokenEntity
import de.flavormate.features.token.enums.TokenType
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class TokenRepository : PanacheRepositoryBase<TokenEntity, String> {
  fun findAllByType(type: TokenType): List<TokenEntity> {
    val params = mutableMapOf("type" to type)

    return list("type = :type", params)
  }

  fun findByToken(token: String): TokenEntity? {
    val params = mutableMapOf("token" to token)

    return find("id = :token", params).firstResult()
  }

  fun findByTokenAndResource(token: String, resource: String): TokenEntity? {
    val params = mutableMapOf("resource" to resource, "token" to token)

    return find("securedResource = :resource and id = :token", params).firstResult()
  }

  fun verifyRefreshToken(hashedJWT: String, securedResource: String): Boolean {
    val params = mapOf("hashedJWT" to hashedJWT, "securedResource" to securedResource)

    return find(
        "securedResource = :securedResource and id = :hashedJWT and revoked = false",
        params,
      )
      .firstResult()
      ?.valid ?: false
  }

  fun revokeToken(hashedJWT: String): Boolean {
    val params = mutableMapOf("hashedJWT" to hashedJWT)

    return update("revoked = true where id = :hashedJWT", params) > 0
  }

  fun findAllRevoked(): List<String> {
    return find("select id from TokenEntity where revoked = true")
      .project(String::class.java)
      .list()
  }

  fun findIssuer(id: String): AccountEntity? {
    val params = mapOf("id" to id)

    return find("select issuer from TokenEntity where id = :id", params)
      .project(AccountEntity::class.java)
      .firstResult()
  }

  fun findOwnShareTokens(sortBy: Sort, issuerId: String): PanacheQuery<TokenEntity> {
    val params = mapOf("tokenType" to TokenType.SHARE, "issuerId" to issuerId)
    return find("tokenType = :tokenType and issuer.id = :issuerId", sortBy, params)
  }
}
