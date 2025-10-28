/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.openFoodFacts.repositories

import de.flavormate.extensions.openFoodFacts.dao.models.OFFProductEntity
import de.flavormate.extensions.openFoodFacts.enums.OFFProductState
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class OFFProductRepository : PanacheRepositoryBase<OFFProductEntity, String> {
  fun existsById(id: String): Boolean {
    val params = mapOf("id" to id)
    return count("id = :id", params) > 0
  }

  fun findByState(state: OFFProductState): PanacheQuery<OFFProductEntity> {
    val params = mapOf("state" to state)
    return find("state = :state", params)
  }
}
