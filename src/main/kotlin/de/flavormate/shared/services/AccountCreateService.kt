/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.services

import de.flavormate.exceptions.FConflictException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.role.repositories.RoleRepository
import io.quarkus.elytron.security.common.BcryptUtil
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class AccountCreateService(
  private val accountRepository: AccountRepository,
  private val roleRepository: RoleRepository,
) {
  fun createAccount(
    username: String,
    displayName: String,
    password: String,
    email: String,
    enabled: Boolean = false,
    verified: Boolean = false,
  ): AccountEntity {
    val existsByEmail = accountRepository.existsByEmail(email)
    val existsByUsername = accountRepository.existsByUsername(username)

    if (existsByEmail || existsByUsername) {
      throw FConflictException(message = "Account already exists!")
    }

    val role =
      roleRepository.findByRole(RoleTypes.User)
        ?: throw FNotFoundException(message = "Role not found!")

    return AccountEntity()
      .apply {
        this.displayName = displayName
        this.username = username
        this.email = email
        this.password = BcryptUtil.bcryptHash(password)
        this.enabled = enabled
        this.verified = verified
        this.roles = mutableSetOf(role)
        this.ownedBy = this
      }
      .also { accountRepository.persist(it) }
  }
}
