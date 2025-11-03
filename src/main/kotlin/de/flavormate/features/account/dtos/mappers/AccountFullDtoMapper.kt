/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.dtos.mappers

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.account.dtos.models.AccountFullDto
import de.flavormate.shared.interfaces.BasicMapper

object AccountFullDtoMapper : BasicMapper<AccountEntity, AccountFullDto>() {
  override fun mapNotNullBasic(input: AccountEntity): AccountFullDto =
    AccountFullDto(
      id = input.id,
      displayName = input.displayName,
      avatar = input.avatar?.let { AccountFileDtoMapper.mapBasic(input = it) },
      username = input.username,
      diet = input.diet,
      email = input.email,
      enabled = input.enabled,
      verified = input.verified,
      firstLogin = input.firstLogin,
      createdOn = input.createdOn,
      roles = input.roles.map { it.role.name },
    )
}
