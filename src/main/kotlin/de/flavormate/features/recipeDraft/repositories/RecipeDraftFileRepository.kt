/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.repositories

import de.flavormate.features.recipeDraft.daos.models.RecipeDraftFileEntity
import de.flavormate.shared.interfaces.CRepository
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecipeDraftFileRepository : CRepository<RecipeDraftFileEntity>(RecipeDraftFileEntity::class) {
  fun findByRecipeDraft(id: String, sort: Sort): PanacheQuery<RecipeDraftFileEntity> {
    val params = mapOf("id" to id)
    return find(
      query = "select f from RecipeDraftFileEntity f join f.recipeDraft rd where rd.id = :id",
      sort = sort,
      params = params,
    )
  }
}
