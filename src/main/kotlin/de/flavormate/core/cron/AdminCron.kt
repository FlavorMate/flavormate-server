/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.cron

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.role.repositories.RoleRepository
import de.flavormate.shared.services.AccountCreateService
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class AdminCron(
  private val accountRepository: AccountRepository,
  private val roleRepository: RoleRepository,
  private val accountCreateService: AccountCreateService,
  private val flavorMateProperties: FlavorMateProperties,
) {

  @Startup
  @Transactional
  fun createAdminAccount() {
    Log.info("Creating admin account if non existent")

    if (accountRepository.count() > 0L) return

    Log.info("Admin account does not exist. Creating one.")

    val adminProperties = flavorMateProperties.general().admin()

    val account =
      accountCreateService.createAccount(
        username = adminProperties.username(),
        displayName = adminProperties.displayName(),
        password = adminProperties.password(),
        email = adminProperties.email(),
        enabled = true,
        verified = true,
      )

    val role =
      roleRepository.findByRole(RoleTypes.Admin)
        ?: throw FNotFoundException(message = "Role not found!")

    account.roles.add(role)

    accountRepository.persist(account)

    Log.info("Admin account created successfully")
  }
}
