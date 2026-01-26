/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.repositories

import de.flavormate.core.auth.dao.models.SessionEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AccountSessionRepository : PanacheRepositoryBase<SessionEntity, String> {
  fun findByTokenHashAndAccountId(tokenHash: String, accountId: String): SessionEntity? {
    val params = mapOf("tokenHash" to tokenHash, "accountId" to accountId)
    return find("tokenHash = :tokenHash and accountId = :accountId", params).firstResult()
  }

  fun findAllByOwner(sort: Sort, accountId: String): PanacheQuery<SessionEntity> {
    val params = mapOf("accountId" to accountId)
    return find(query = "accountId = :accountId", sort = sort, params = params)
  }

  fun deleteByAccountIdAndId(accountId: String, id: String): Boolean {
    val params = mapOf("accountId" to accountId, "id" to id)
    val response = delete(query = "accountId = :accountId and id = :id", params = params)
    return response > 0
  }

  fun deleteByTokenHash(tokenHash: String): Boolean {
    val params = mapOf("tokenHash" to tokenHash)
    val response = delete(query = "tokenHash = :tokenHash", params = params)
    return response > 0
  }

  fun deleteByAccountId(accountId: String): Boolean {
    val params = mapOf("accountId" to accountId)
    val response = delete(query = "accountId = :accountId", params = params)
    return response > 0
  }

  fun deleteAllSessionsButCurrent(accountId: String, tokenHash: String): Boolean {
    val params = mapOf("accountId" to accountId, "tokenHash" to tokenHash)
    delete(query = "accountId = :accountId and tokenHash != :tokenHash", params = params)
    return true
  }
}
