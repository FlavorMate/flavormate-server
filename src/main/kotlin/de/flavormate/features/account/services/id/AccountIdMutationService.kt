/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.account.services.id

import de.flavormate.exceptions.FConflictException
import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.dtos.models.AccountUpdateDto
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.shared.services.AuthorizationDetails
import io.quarkus.elytron.security.common.BcryptUtil
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class AccountIdMutationService(
  private val accountRepository: AccountRepository,
  private val authorizationDetails: AuthorizationDetails,
) {

  fun putAccountsId(id: String, form: AccountUpdateDto) {
    val account =
      accountRepository.findById(id) ?: throw FNotFoundException(message = "Account not found!")

    if (!authorizationDetails.isAdminOrOwner(account))
      throw FForbiddenException(message = "You are not allowed to update this account!")

    if (form.diet != null) {
      account.diet = form.diet
    }

    if (form.email != null) {
      account.email = form.email.trim()
    }

    if (form.oldPassword != null && form.newPassword != null) {
      if (!BcryptUtil.matches(form.oldPassword, account.password)) {
        throw FConflictException(message = "Old password does not match!")
      }

      account.password = BcryptUtil.bcryptHash(form.newPassword)
    }

    accountRepository.persist(account)
  }
}
