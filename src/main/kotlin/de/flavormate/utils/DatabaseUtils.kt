/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.utils

import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.logging.Log
import io.quarkus.narayana.jta.QuarkusTransaction

object DatabaseUtils {
  fun <T : Any> batchedRun(
    query: PanacheQuery<T>,
    run: (items: List<T>, currentBatch: Int) -> Unit,
  ) {
    var pageIndex = 0
    var hasMore = true
    val batchSize = 10

    while (hasMore) {
      val page = query.page(pageIndex, batchSize)
      val items = page.list()
      hasMore = page.hasNextPage()

      val currentBatch = (pageIndex) * batchSize

      run(items, currentBatch)

      pageIndex++
    }
  }

  fun <T : Any> batchedRunSingle(
    query: PanacheQuery<T>,
    run: (item: T, currentIndex: Int) -> Unit,
  ) {
    val batchSize = 10
    var processedCount = 1L

    while (true) {
      // Always re-fetch the "front" of the moving target set.
      // This avoids skipping when the action changes the query membership.
      val items = query.page(0, batchSize).list()
      if (items.isEmpty()) break

      for (item in items) {
        runCatching {
            QuarkusTransaction.requiringNew().timeout(300).run { run(item, processedCount.toInt()) }
          }
          .onFailure { Log.error("Could not process item $processedCount: ${it.message}") }
        processedCount++
      }
    }
  }
}
