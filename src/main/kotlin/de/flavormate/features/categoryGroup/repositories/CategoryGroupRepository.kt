/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.categoryGroup.repositories

import de.flavormate.features.categoryGroup.daos.models.CategoryGroupEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CategoryGroupRepository : PanacheRepositoryBase<CategoryGroupEntity, String> {
  override fun findAll(sort: Sort): PanacheQuery<CategoryGroupEntity> {
    return find(query = "select c from CategoryGroupEntity c", sort = sort)
  }
}
