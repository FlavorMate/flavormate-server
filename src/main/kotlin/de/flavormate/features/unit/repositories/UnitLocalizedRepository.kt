/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.unit.repositories

import de.flavormate.features.unit.daos.models.UnitLocalizedEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UnitLocalizedRepository : PanacheRepositoryBase<UnitLocalizedEntity, String> {
  fun findByLanguage(sort: Sort, language: String): PanacheQuery<UnitLocalizedEntity> {
    val params = mapOf("language" to language)

    return find(
      query = "select u from UnitLocalizedEntity u where lower(u.language) = lower(:language)",
      sort = sort,
      params = params,
    )
  }

  fun findByLabelAndLanguage(label: String, language: String): PanacheQuery<UnitLocalizedEntity> {
    val params = mapOf("label" to label, "language" to language)
    return find(
      "select u from UnitLocalizedEntity u where (lower(u.labelSg) = lower(:label) OR lower(u.labelPl) = lower(:label) OR lower(u.labelSgAbrv) = lower(:label) OR lower(u.labelPlAbrv) = lower(:label)) AND language = :language",
      params,
    )
  }
}
