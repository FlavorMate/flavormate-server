/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.utils

import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery

object DatabaseUtils {
  fun <T : Any> batchedRun(query: PanacheQuery<T>, run: (items: List<T>) -> Unit) {
    var pageIndex = 0
    var hasMore = true
    val batchSize = 10

    while (hasMore) {
      val page = query.page(pageIndex, batchSize)
      val items = page.list()
      hasMore = page.hasNextPage()

      run(items)

      pageIndex++
    }
  }
}
