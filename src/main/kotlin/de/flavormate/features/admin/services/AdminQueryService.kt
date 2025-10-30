/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.admin.services

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.features.account.dtos.mappers.AccountFullDtoMapper
import de.flavormate.features.account.dtos.models.AccountFullDto
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class AdminQueryService(
  private val accountRepository: AccountRepository,
  private val authorizationDetails: AuthorizationDetails,
) {
  fun getAdminAccounts(pagination: Pagination): PageableDto<AccountFullDto> {
    if (!authorizationDetails.isAdmin())
      throw FForbiddenException(message = "You are not allowed to access this resource")

    val query = accountRepository.findAll(sort = pagination.sortRequest(AllowedSorts.accounts))

    return PageableDto.fromQuery(
      dataQuery = query,
      page = pagination.pageRequest,
      mapper = AccountFullDtoMapper::mapNotNullBasic,
      countQuery = null,
    )
  }
}
