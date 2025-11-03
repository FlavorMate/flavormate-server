/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.services

import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class AccountService(val queryService: AccountQueryService) {
  // Query
  fun getAccounts(pagination: Pagination) = queryService.getAccounts(pagination = pagination)

  fun getAccountsSelf() = queryService.getAccountsSelf()

  fun getAccountsSearch(query: String, pagination: Pagination) =
    queryService.getAccountsSearch(query = query, pagination = pagination)
}
