/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.account.services

import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.dtos.mappers.AccountFullDtoMapper
import de.flavormate.features.account.dtos.mappers.AccountPreviewDtoMapper
import de.flavormate.features.account.dtos.models.AccountFullDto
import de.flavormate.features.account.dtos.models.AccountPreviewDto
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class AccountQueryService(
  private val accountRepository: AccountRepository,
  private val authorizationDetails: AuthorizationDetails,
) {
  fun getAccounts(pagination: Pagination): PageableDto<AccountPreviewDto> {
    val query = accountRepository.findAll(sort = pagination.sortRequest(AllowedSorts.accounts))

    return PageableDto.fromQuery(
      dataQuery = query,
      page = pagination.pageRequest,
      mapper = AccountPreviewDtoMapper::mapNotNullBasic,
      countQuery = null,
    )
  }

  fun getAccountsSelf(): AccountFullDto {
    val entity = authorizationDetails.getSelf()

    if (!authorizationDetails.isAdminOrOwner(target = entity))
      throw FNotFoundException(message = "You are not allowed to access this account!", id = "")

    return AccountFullDtoMapper.mapNotNullBasic(input = entity)
  }

  fun getAccountsSearch(query: String, pagination: Pagination): PageableDto<AccountPreviewDto> {
    val dataQuery =
      accountRepository.findBySearch(
        query = query,
        sort = pagination.sortRequest(AllowedSorts.accounts),
      )

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = AccountPreviewDtoMapper::mapNotNullBasic,
    )
  }
}
