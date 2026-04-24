/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.search.repositories

import de.flavormate.features.search.models.SearchEntity
import de.flavormate.features.search.models.SearchFilter
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class SearchRepository : PanacheRepositoryBase<SearchEntity, String> {
  fun findByQuery(
    q: String,
    ownerId: String,
    language: String,
    filter: List<SearchFilter>,
  ): PanacheQuery<SearchEntity> {
    val pattern = "%${q.lowercase()}%"

    return find(
      """
      lower(label) like ?1
      and (
           source <> 'book'
           or bookVisible = true
           or bookOwnedBy = ?2
      )
      and (
          source <> 'category'
          or categoryLanguage = ?3
      )

      and source in ?4

      order by length(lower(label)), lower(label)
      """
        .trimIndent(),
      pattern,
      ownerId,
      language,
      filter,
    )
  }
}
