/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.admin.services

import de.flavormate.features.admin.dto.AccountForm
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional

@RequestScoped
class AdminService(val mutationService: AdminMutationService, val queryService: AdminQueryService) {
  // Queries
  fun getAdminAccounts(pagination: Pagination) =
    queryService.getAdminAccounts(pagination = pagination)

  // Mutations
  @Transactional
  fun setPassword(id: String, newPassword: String) =
    mutationService.setPassword(id = id, newPassword = newPassword)

  @Transactional fun toggleActiveState(id: String) = mutationService.toggleActiveState(id = id)

  @Transactional fun deleteAccount(id: String) = mutationService.deleteAccount(id = id)

  @Transactional
  fun createAccount(form: AccountForm) =
    mutationService.createAccount(
      displayName = form.displayName,
      username = form.username,
      password = form.password,
      email = form.email,
    )
}
