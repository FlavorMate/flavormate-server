/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.models.api

import de.flavormate.shared.enums.SearchOrderBy
import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import org.jboss.resteasy.reactive.RestQuery

data class Pagination(
  @RestQuery("page") var page: Int = 0,
  @RestQuery("pageSize") var pageSize: Int = 6,
  @RestQuery("orderBy") var orderBy: SearchOrderBy? = null,
  @RestQuery("orderDirection") var orderDirection: Sort.Direction? = null,
) {

  fun sortRequest(map: Map<SearchOrderBy, String>): Sort {
    if (map.containsKey(orderBy)) {

      // Used to enable case-insensitive sorting
      // This is only allowed on String values
      val column =
        when (orderBy) {
          SearchOrderBy.CreatedOn -> map[orderBy]!!
          SearchOrderBy.Visible -> map[orderBy]!!
          else -> "lower(${map[orderBy]!!})"
        }
      return Sort.by(column, orderDirection ?: Sort.Direction.Ascending).disableEscaping()
    }

    return Sort.empty()
  }

  val pageRequest
    get(): Page = if (pageSize > 0) Page.of(page, pageSize) else Page.ofSize(Integer.MAX_VALUE)
}
