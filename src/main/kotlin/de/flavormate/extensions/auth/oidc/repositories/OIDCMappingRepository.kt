/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.repositories

import de.flavormate.extensions.auth.oidc.dao.models.OIDCMappingEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class OIDCMappingRepository : PanacheRepositoryBase<OIDCMappingEntity, String> {
  fun findByJwt(issuer: String, subject: String): OIDCMappingEntity? {
    val params = mapOf("issuer" to issuer, "subject" to subject)

    return find("issuer = :issuer and subject = :subject", params).firstResult()
  }
}
