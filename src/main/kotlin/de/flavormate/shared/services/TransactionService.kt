/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.services

import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Status
import jakarta.transaction.Synchronization
import jakarta.transaction.TransactionManager

@RequestScoped
class TransactionService(private val transactionManager: TransactionManager) {

  val pendingOperations = mutableListOf<() -> Unit>()

  fun initialize() {
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
  }
}
