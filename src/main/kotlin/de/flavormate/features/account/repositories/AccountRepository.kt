/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.account.repositories

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.story.daos.models.StoryEntity
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.enums.SearchOrderBy
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AccountRepository : PanacheRepositoryBase<AccountEntity, String> {
  override fun findAll(sort: Sort): PanacheQuery<AccountEntity> {
    val sortColumn = sort.columns.firstOrNull()

    if (sortColumn?.name == AllowedSorts.adminAccounts.getValue(SearchOrderBy.LastActivity)) {
      val aggregateFn = if (sortColumn.direction == Sort.Direction.Ascending) "min" else "max"

      return find(
        query =
          "from AccountEntity a order by (select $aggregateFn(s.lastModifiedOn) from SessionEntity s where s.account = a)"
      )
    }

    return find(query = "select a from AccountEntity a left join fetch a.sessions s", sort = sort)
  }

  fun findByNoRole(role: RoleTypes): PanacheQuery<AccountEntity> {
    val params = mapOf("role" to role)
    return find(
      query =
        "select a from AccountEntity a where not exists (select 1 from a.roles r where r.role = :role)",
      params = params,
    )
  }

  fun findByUsername(username: String): AccountEntity? {
    val params = mapOf("username" to username)
    return find(query = "username = :username", params = params).firstResult()
  }

  fun findByEmail(email: String): AccountEntity? {
    val params = mapOf("email" to email)
    return find(query = "email = :email", params = params).firstResult()
  }

  fun existsByUsername(username: String): Boolean {
    val params = mapOf("username" to username)
    return count(query = "username = :username", params = params) > 0
  }

  fun existsByEmail(email: String): Boolean {
    val params = mapOf("email" to email)

    return count("email = :email", params) > 0
  }

  fun findBySearch(sort: Sort, query: String): PanacheQuery<AccountEntity> {
    val params = mapOf("query" to query)
    return find(
      query =
        "select a from AccountEntity a where lower(a.displayName) like lower(concat('%', :query, '%'))",
      sort = sort,
      params = params,
    )
  }

  fun findRecipesByAccountId(id: String, sort: Sort): PanacheQuery<RecipeEntity> {
    val params = mapOf("id" to id)
    return find(
        query = "select r from AccountEntity a join a.recipes r where a.id = :id",
        sort = sort,
        params = params,
      )
      .project(RecipeEntity::class.java)
  }

  fun findStoriesByAccountId(id: String, sort: Sort): PanacheQuery<StoryEntity> {
    val params = mapOf("id" to id)
    return find(
        query = "select s from AccountEntity a join a.stories s where a.id = :id",
        sort = sort,
        params = params,
      )
      .project(StoryEntity::class.java)
  }
}
