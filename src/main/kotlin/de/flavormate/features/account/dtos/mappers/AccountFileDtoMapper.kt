/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.account.dtos.mappers

import de.flavormate.features.account.controllers.AccountController
import de.flavormate.features.account.dao.models.AccountFileEntity
import de.flavormate.features.account.dtos.models.AccountFileDto
import de.flavormate.shared.interfaces.BasicMapper
import jakarta.ws.rs.core.UriBuilder

object AccountFileDtoMapper : BasicMapper<AccountFileEntity, AccountFileDto>() {
  override fun mapNotNullBasic(input: AccountFileEntity) =
    AccountFileDto(
      id = input.id,
      path =
        UriBuilder.fromResource(AccountController::class.java)
          .path(AccountController::class.java, AccountController::getAccountsAvatar.name)
          .build(input.ownedById, input.id)
          .toString(),
    )
}
