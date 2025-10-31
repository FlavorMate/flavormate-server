/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.category.repositories

import de.flavormate.features.category.daos.models.CategoryEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CategoryRepository : PanacheRepositoryBase<CategoryEntity, String> {

  override fun findAll(sort: Sort): PanacheQuery<CategoryEntity> {
      return find(
          query = "select c from CategoryEntity c left join  c.localizations l",
      sort = sort,
    )
  }

    fun findAll(sort: Sort, language: String): PanacheQuery<CategoryEntity> {
        val params = mapOf("language" to language)
        return find(
            query = "select c from CategoryEntity c left join c.localizations l where l.id.language = :language and c.recipes is not empty",
            sort = sort,
            params = params,
        )
  }

    fun findByLocalizedLabelAndLanguage(label: String): CategoryEntity? {
        val params = mapOf("label" to label)
    return find(
        "select c from CategoryEntity c left join c.localizations l where lower(l.value) = lower(:label)",
        params,
      )
      .firstResult()
  }

  fun findBySearch(sort: Sort, language: String, query: String): PanacheQuery<CategoryEntity> {
    val params = mapOf("language" to language, "query" to query)
    return find(
        "select c from CategoryEntity c left join c.localizations l where l.id.language = :language and lower(l.value) like lower(concat('%', :query, '%'))",
      sort,
      params,
    )
  }

  fun countRecipes(id: String): PanacheQuery<Long> {
    val params = mapOf("id" to id)

    return find("select count(r) from CategoryEntity c join c.recipes r where c.id = :id", params)
      .project(Long::class.java)
  }

  fun findRecipes(sort: Sort, id: String): PanacheQuery<RecipeEntity> {
    val params = mapOf("id" to id)

    return find("select r from CategoryEntity c join c.recipes r where c.id = :id", sort, params)
      .project(RecipeEntity::class.java)
  }

  fun findByIdsIn(ids: List<String>): List<CategoryEntity> {
    val params = mapOf("ids" to ids)

    return list(query = "id in :ids", params = params)
  }
}
