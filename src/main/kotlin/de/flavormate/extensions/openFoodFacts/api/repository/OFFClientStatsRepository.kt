/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.openFoodFacts.api.repository

import de.flavormate.extensions.openFoodFacts.api.daos.models.OFFClientStatsEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDateTime

@ApplicationScoped
class OFFClientStatsRepository : PanacheRepositoryBase<OFFClientStatsEntity, String> {
  fun findCurrent(): OFFClientStatsEntity {
    val params = mapOf("timestamp" to LocalDateTime.now().withSecond(0).withNano(0))
    return find("timestamp = :timestamp", params).firstResult() ?: OFFClientStatsEntity()
  }
}
