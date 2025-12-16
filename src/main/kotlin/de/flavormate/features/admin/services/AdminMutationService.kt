/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.admin.services

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.shared.services.AccountCreateService
import de.flavormate.shared.services.AuthorizationDetails
import io.quarkus.elytron.security.common.BcryptUtil
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class AdminMutationService(
  private val accountRepository: AccountRepository,
  private val authorizationDetails: AuthorizationDetails,
  private val accountCreateService: AccountCreateService,
) {

  fun setPassword(id: String, newPassword: String) {
    val account =
      accountRepository.findById(id) ?: throw FNotFoundException(message = "Account not found!")

    if (!authorizationDetails.isAdmin())
      throw FForbiddenException(
        message = "You are not allowed to update the password of this account!"
      )

    account.password = BcryptUtil.bcryptHash(newPassword)

    accountRepository.persist(account)
  }

  fun toggleActiveState(id: String) {
    val account =
      accountRepository.findById(id) ?: throw FNotFoundException(message = "Account not found!")

    if (!authorizationDetails.isAdmin())
      throw FForbiddenException(
        message = "You are not allowed to update the state of this account!"
      )

    account.enabled = !account.enabled

    accountRepository.persist(account)
  }

  fun deleteAccount(id: String) {
    if (!authorizationDetails.isAdmin())
      throw FForbiddenException(message = "You are not allowed to delete this account!")

    accountRepository.deleteById(id)
  }

  fun createAccount(displayName: String, username: String, password: String, email: String) {
    if (!authorizationDetails.isAdmin())
      throw FForbiddenException(message = "You are not allowed to create an account!")

    accountCreateService.createAccount(
      username = username,
      displayName = displayName,
      password = password,
      email = email,
      enabled = true,
      verified = true,
    )
  }
}
