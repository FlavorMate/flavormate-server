/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.book.repositories

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.book.daos.models.BookEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class BookRepository : PanacheRepositoryBase<BookEntity, String> {

  override fun findAll(sort: Sort): PanacheQuery<BookEntity> {
    return find(query = "select b from BookEntity b", sort = sort)
  }

  fun findBySearch(sort: Sort, accountId: String, query: String): PanacheQuery<BookEntity> {
    val params = mapOf("accountId" to accountId, "query" to query)

    return find(
      query =
        "select b from BookEntity b where (ownedById = :accountId or visible = true) and lower(b.label) like lower(concat('%', :query, '%'))",
      sort = sort,
      params = params,
    )
  }

  fun search(email: String, searchTerm: String, sortBy: Sort): PanacheQuery<BookEntity> {
    return find(
      "select b from BookEntity b where (b.ownedBy.email = ?1 or b.visible = true) and lower(b.label) like lower(concat('%', ?2, '%')) ",
      sortBy,
      email,
      searchTerm,
    )
  }

  fun findOwn(sort: Sort, id: String): PanacheQuery<BookEntity> {
    val params = mapOf("id" to id)

    return find(
      query = "select b from BookEntity b where b.ownedById = :id ",
      sort = sort,
      params = params,
    )
  }

  fun countOwn(id: String): PanacheQuery<Long> {
    val params = mapOf("id" to id)

    return find(
        query = "select count(b) from BookEntity b where b.ownedById = :id",
        params = params,
      )
      .project(Long::class.java)
  }

  fun findOwnView(sort: Sort, id: String): PanacheQuery<BookEntity> {
    val params = mapOf("id" to id)

    return find(
      query =
        "select b from BookEntity b left join b.subscriber bs where b.ownedById = :id or bs.id = :id",
      sort = sort,
      params = params,
    )
  }

  fun countOwnView(id: String): PanacheQuery<Long> {
    val params = mapOf("id" to id)

    return find(
        query =
          "select count(b) from BookEntity b left join b.subscriber bs where b.ownedById = :id or bs.id = :id",
        params = params,
      )
      .project(Long::class.java)
  }

  fun findRecipes(sort: Sort, id: String): PanacheQuery<RecipeEntity> {
    val params = mapOf("id" to id)

    return find(
        query = "select r from BookEntity b join b.recipes r where b.id = :id",
        sort = sort,
        params = params,
      )
      .project(RecipeEntity::class.java)
  }

  override fun deleteById(id: String): Boolean {
    return delete("id = ?1", id) > 0
  }

  fun findByIds(ids: List<String>, sort: Sort): PanacheQuery<BookEntity> {
    val params = mapOf("ids" to ids)
    return find(
      query = "select b from BookEntity b where b.id in :ids",
      sort = sort,
      params = params,
    )
  }

  fun findSubscribers(id: String, sort: Sort): PanacheQuery<AccountEntity> {
    val params = mapOf("id" to id)

    return find(
        query = "select s from BookEntity b join b.subscriber s where b.id = :id",
        sort = sort,
        params = params,
      )
      .project(AccountEntity::class.java)
  }

  fun findSubscriber(id: String, accountId: String): Boolean {
    val params = mapOf("id" to id, "accountId" to accountId)

    return count(
      query =
        "select s from BookEntity b join b.subscriber s where b.id = :id and s.id = :accountId",
      params = params,
    ) > 0
  }
}
