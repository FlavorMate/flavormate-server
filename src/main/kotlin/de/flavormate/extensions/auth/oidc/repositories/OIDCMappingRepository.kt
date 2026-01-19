/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.repositories

import de.flavormate.extensions.auth.oidc.dao.models.OIDCMappingEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class OIDCMappingRepository : PanacheRepositoryBase<OIDCMappingEntity, String> {
  fun findByAccountId(accountId: String, sort: Sort): PanacheQuery<OIDCMappingEntity> {
    val params = mapOf("accountId" to accountId)
    return find(
      "select m from OIDCMappingEntity m left join m.provider p where m.accountId = :accountId",
      sort = sort,
      params = params,
    )
  }

  fun deleteByAccountIdAndProviderId(accountId: String, providerId: String): Boolean {
    val params = mapOf("accountId" to accountId, "providerId" to providerId)
    val response = delete("accountId = :accountId and provider.id = :providerId", params = params)
    return response > 0
  }

  fun findByIssuerAndAudiencesAndSubject(
    issuer: String,
    audiences: Set<String>?,
    subject: String,
  ): OIDCMappingEntity? {
    if (audiences.isNullOrEmpty()) return null

    val params = mapOf("issuer" to issuer, "audiences" to audiences, "subject" to subject)
    return find(
        "select m from OIDCMappingEntity m left join m.provider p where p.issuer = :issuer and p.clientId in :audiences and m.subject = :subject",
        params,
      )
      .firstResult()
  }
}
