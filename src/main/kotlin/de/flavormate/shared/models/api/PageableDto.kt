/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.models.api

import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.panache.common.Page
import kotlin.math.ceil

data class PageableDto<Dto>(val metadata: Metadata, val data: List<Dto>) {

  companion object {
    fun <Dao : Any, Dto : Any> fromQuery(
      dataQuery: PanacheQuery<Dao>,
      page: Page,
      countQuery: PanacheQuery<Long>? = null,
      mapper: (Dao) -> Dto,
    ): PageableDto<Dto> {

      val data = dataQuery.page(page = page).list()
      val count = countQuery?.firstResult() ?: dataQuery.count()

      val metadata =
        Metadata(
          totalElements = count,
          pageSize = page.size,
          currentPage = page.index + 1,
          totalPages = ceil(count.toDouble() / page.size).toInt(),
        )

      return PageableDto(metadata, data.map(mapper))
    }
  }

  data class Metadata(
    val totalElements: Long,
    val pageSize: Int,
    val currentPage: Int,
    val totalPages: Int,
  )
}
