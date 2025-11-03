/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.storyDraft.repositories

import de.flavormate.features.storyDraft.daos.models.StoryDraftEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class StoryDraftRepository : PanacheRepositoryBase<StoryDraftEntity, String> {

  override fun findAll(sort: Sort): PanacheQuery<StoryDraftEntity> {
    return find(query = "select s from StoryDraftEntity s", sort = sort)
  }

  fun findByOriginId(id: String): StoryDraftEntity? {
    return find("originId = ?1", id).firstResult()
  }

  fun existsByOriginId(id: String): Boolean {
    return count("originId = ?1", id) > 0
  }

  fun findAllByOwnedBy(id: String, sort: Sort): PanacheQuery<StoryDraftEntity> {
    val params = mapOf("id" to id)

    return find(
      query = "select s from StoryDraftEntity s where s.ownedById = :id",
      sort = sort,
      params = params,
    )
  }
}
