/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.repositories

import de.flavormate.extensions.auth.oidc.dao.models.OIDCProviderEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class OIDCProviderRepository : PanacheRepositoryBase<OIDCProviderEntity, String> {
  fun findAllEnabled(): List<OIDCProviderEntity> {
    return list("enabled = true")
  }

  fun findByIssuerAndClientId(issuer: String, clientId: String): OIDCProviderEntity? {
    val params = mapOf("issuer" to issuer, "clientId" to clientId)
    return find("issuer = :issuer and clientId = :clientId", params).firstResult()
  }

  fun findByIssuerAndClientIds(issuer: String, audiences: Set<String>?): OIDCProviderEntity? {
    if (audiences.isNullOrEmpty()) return null

    val params = mapOf("issuer" to issuer, "audiences" to audiences)
    return find("issuer = :issuer and clientId in :audiences", params).firstResult()
  }
}
