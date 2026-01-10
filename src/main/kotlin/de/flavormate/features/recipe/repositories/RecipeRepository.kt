/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.repositories

import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipe.daos.models.RecipeFileEntity
import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecipeRepository : PanacheRepositoryBase<RecipeEntity, String> {

  override fun findAll(sort: Sort): PanacheQuery<RecipeEntity> {
    return find(query = "select r from RecipeEntity r", sort = sort)
  }

  fun countByDietIn(diet: Set<Diet>): Long {
    return count("diet in ?1", diet)
  }

  fun findFiles(sort: Sort, id: String): PanacheQuery<RecipeFileEntity> {
    val params = mapOf("id" to id)

    return find(
        query = "select f from RecipeEntity r join r.files f where r.id = :id",
        sort = sort,
        params = params,
      )
      .project(RecipeFileEntity::class.java)
  }

  fun findBySearch(sort: Sort, query: String): PanacheQuery<RecipeEntity> {
    val params = mapOf("query" to query)

    return find(
      query = "lower(label) like lower(concat('%', :query, '%')) ",
      sort = sort,
      params = params,
    )
  }

  fun findRandomRecipeByDiet(diets: Set<Diet>, course: Course?): PanacheQuery<RecipeEntity> {
    val params = mapOf("diets" to diets, "course" to course)

    return find(
      query =
        "diet in :diets and (:course is null or course = :course) order by function('RANDOM')",
      params = params,
    )
  }

  fun countRandomRecipeByDiet(diets: Set<Diet>, course: Course?): PanacheQuery<Long> {
    val params = mapOf("diets" to diets, "course" to course)

    return find(
        query =
          "select count(r) from RecipeEntity r where r.diet in :diets and (:course is null or r.course = :course) order by function('RANDOM')",
        params = params,
      )
      .project(Long::class.java)
  }

  fun findAllWithUrl(): PanacheQuery<RecipeEntity> {
    return find(query = "url is not null")
  }

  fun findAllWithDescription(): PanacheQuery<RecipeEntity> {
    return find(query = "description is not null")
  }

  override fun deleteById(id: String): Boolean {
    return delete("id = ?1", id) > 0
  }
}
