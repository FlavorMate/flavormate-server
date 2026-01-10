/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.unit.repositories

import de.flavormate.features.unit.daos.models.UnitConversionEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UnitConversionRepository : PanacheRepositoryBase<UnitConversionEntity, String> {
  fun findFactor(source: String, target: String): Double? {
    return find("id.source.id = ?1 and id.target.id = ?2", source, target).firstResult()?.factor
  }

  fun findConversions(source: String): List<String> {
    return find(
        "select ui.id.target.id from UnitConversionEntity uc where uc.id.source.id = ?1",
        source,
      )
      .project(String::class.java)
      .list()
  }
}
