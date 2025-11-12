/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.highlight.repositories

import de.flavormate.features.highlight.daos.models.HighlightEntity
import de.flavormate.shared.enums.Diet
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDate

@ApplicationScoped
class HighlightRepository : PanacheRepositoryBase<HighlightEntity, String> {
  override fun findAll(sort: Sort): PanacheQuery<HighlightEntity> {
    return find(query = "select h from HighlightEntity h", sort = sort)
  }

  fun findAllByDiet(sort: Sort, diet: Diet): PanacheQuery<HighlightEntity> {
    val params = mapOf("diet" to diet)

    return find(
      query = "select h from HighlightEntity h where h.diet = :diet",
      sort = sort,
      params = params,
    )
  }

  fun countAllByDiet(diet: Diet): PanacheQuery<Long> {
    val params = mapOf("diet" to diet)
    return find(
        query = "select count(h) from HighlightEntity h where diet = :diet",
        params = params,
      )
      .project(Long::class.java)
  }

  fun findInRangeByDiet(daysToGenerate: Long, diet: Diet): List<HighlightEntity> {
    return list(
      "date between ?1 and ?2 and diet = ?3",
      LocalDate.now().minusDays(daysToGenerate),
      LocalDate.now(),
      diet,
    )
  }

  fun deleteOldEntriesById(diet: Diet, deleteBefore: LocalDate): Long {
    val params = mapOf("diet" to diet, "deleteBefore" to deleteBefore)
    return delete("diet = :diet and date <= :deleteBefore", params)
  }
}
