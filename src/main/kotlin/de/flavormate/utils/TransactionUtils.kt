/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.utils

import jakarta.transaction.Status
import jakarta.transaction.Synchronization
import jakarta.transaction.TransactionManager

/**
 * Utility class providing functions for managing operations to be executed after a transaction is
 * successfully committed.
 */
object TransactionUtils {
  /**
   * Registers and returns a list of operations to be executed after a transaction has been
   * successfully committed.
   *
   * @param transactionManager the TransactionManager used to handle transactions and trigger the
   *   after-commit operations.
   * @return a list of pending operations (as lambdas) that will be executed if the transaction is
   *   successfully committed.
   */
  fun runAfterCommit(transactionManager: TransactionManager): MutableList<() -> Unit> {
    val pendingOperations = mutableListOf<() -> Unit>()

    transactionManager.transaction.registerSynchronization(
      object : Synchronization {
        override fun beforeCompletion() {}

        override fun afterCompletion(status: Int) {
          if (status == Status.STATUS_COMMITTED) {
            // Only perform file operations if the transaction was successful
            for (pendingOperation in pendingOperations) {
              pendingOperation.invoke()
            }
          }
        }
      }
    )

    return pendingOperations
  }
}
