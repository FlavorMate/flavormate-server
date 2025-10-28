/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.services.id

import de.flavormate.features.account.dtos.models.AccountUpdateDto
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional

@RequestScoped
class AccountIdService(
  private val mutationService: AccountIdMutationService,
  private val queryService: AccountIdQueryService,
) {
  // GET
  fun getAccountsId(id: String) = queryService.getAccountsId(id = id)

  fun getAccountsIdBooks(id: String, pagination: Pagination) =
    queryService.getAccountsIdBooks(id = id, pagination = pagination)

  fun getAccountsIdRecipes(id: String, pagination: Pagination) =
    queryService.getAccountsIdRecipes(id = id, pagination = pagination)

  fun getAccountsIdStories(id: String, pagination: Pagination) =
    queryService.getAccountsIdStories(id = id, pagination = pagination)

  // PUT
  @Transactional
  fun putAccountsId(id: String, form: AccountUpdateDto) =
    mutationService.putAccountsId(id = id, form = form)
}
