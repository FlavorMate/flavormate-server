/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.repositories

import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecipeDraftRepository : PanacheRepositoryBase<RecipeDraftEntity, String> {

  override fun findAll(sort: Sort): PanacheQuery<RecipeDraftEntity> {
    return find(query = "select r from RecipeDraftEntity r", sort = sort)
  }

  fun existsByOriginId(id: String): Boolean {
    val params = mapOf("id" to id)

    return count(query = "originId = :id", params = params) > 0
  }

  fun findByOriginId(id: String): RecipeDraftEntity? {
    val params = mapOf("id" to id)

    return find(query = "originId = :id", params = params).firstResult()
  }

  fun countAllByOwnedBy(id: String): PanacheQuery<Long> {
    val params = mapOf("id" to id)

    return find(
        query = "select count(r) from RecipeDraftEntity r where r.ownedById = :id",
        params = params,
      )
      .project(Long::class.java)
  }

  fun findAllByOwnedBy(id: String, sort: Sort): PanacheQuery<RecipeDraftEntity> {
    val params = mapOf("id" to id)

    return find(
      query = "select r from RecipeDraftEntity r where r.ownedById = :id",
      sort = sort,
      params = params,
    )
  }
}
