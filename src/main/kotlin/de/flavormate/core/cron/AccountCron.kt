/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.cron

import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.role.repositories.RoleRepository
import de.flavormate.utils.DatabaseUtils
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class AccountCron(
  private val accountRepository: AccountRepository,
  private val roleRepository: RoleRepository,
) {

  @Startup
  @Transactional
  fun addIERoles() {
    val roleImport =
      roleRepository.findByRole(RoleTypes.Import)
        ?: throw FNotFoundException(message = "Role not found!")

    val roleExport =
      roleRepository.findByRole(RoleTypes.Export)
        ?: throw FNotFoundException(message = "Role not found!")

    DatabaseUtils.batchedRun(query = accountRepository.findByNoRole(RoleTypes.Import)) { items, _ ->
      Log.info("Assigning import role to users that dont have them yet")

      for (account in items) {
        account.roles.add(roleImport)
        accountRepository.persist(account)
        Log.info("Assigned import role to user: ${account.username}")
      }
    }

    DatabaseUtils.batchedRun(query = accountRepository.findByNoRole(RoleTypes.Export)) { items, _ ->
      Log.info("Assigning export role to users that dont have them yet")

      for (account in items) {
        account.roles.add(roleExport)
        accountRepository.persist(account)
        Log.info("Assigned export role to user: ${account.username}")
      }
    }
  }
}
