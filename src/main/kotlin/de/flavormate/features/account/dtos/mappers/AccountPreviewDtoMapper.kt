/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.dtos.mappers

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.account.dtos.models.AccountPreviewDto
import de.flavormate.shared.interfaces.BasicMapper

object AccountPreviewDtoMapper : BasicMapper<AccountEntity, AccountPreviewDto>() {
  override fun mapNotNullBasic(input: AccountEntity): AccountPreviewDto =
    AccountPreviewDto(
      id = input.id,
      displayName = input.displayName,
      avatar = input.avatar?.let { AccountFileDtoMapper.mapBasic(input = it) },
    )
}
