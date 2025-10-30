/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.story.repositories

import de.flavormate.features.story.daos.models.StoryEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class StoryRepository : PanacheRepositoryBase<StoryEntity, String> {
  override fun findAll(sort: Sort): PanacheQuery<StoryEntity> {
    return find(query = "select s from StoryEntity s", sort = sort)
  }

  fun findBySearch(query: String, sort: Sort): PanacheQuery<StoryEntity> {
    val params = mapOf("query" to query)

    return find(
      query =
        "select s from StoryEntity s where lower(s.label) like lower(concat('%', :query, '%'))",
      sort = sort,
      params = params,
    )
  }
}
