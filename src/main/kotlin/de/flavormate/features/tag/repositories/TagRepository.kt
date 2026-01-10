/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.tag.repositories

import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.tag.daos.models.TagEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class TagRepository : PanacheRepositoryBase<TagEntity, String> {
  override fun findAll(sort: Sort): PanacheQuery<TagEntity> {
    return find("select t from TagEntity t", sort)
  }

  fun findByLabel(label: String): TagEntity? {
    return find("label", label).firstResult()
  }

  fun findBySearch(sort: Sort, query: String): PanacheQuery<TagEntity> {
    val params = mapOf("query" to query)

    return find(
      query = "select t from TagEntity t where lower(t.label) like lower(concat('%', :query, '%'))",
      sort = sort,
      params = params,
    )
  }

  fun findRecipesById(id: String, sort: Sort): PanacheQuery<RecipeEntity> {
    val params = mapOf("id" to id)
    return find(
        "select r from TagEntity t join t.recipes r where t.id = :id",
        sort = sort,
        params = params,
      )
      .project(RecipeEntity::class.java)
  }

  fun deleteOrphanTags(): Long {
    return delete("from TagEntity t where t.recipes is empty")
  }
}
