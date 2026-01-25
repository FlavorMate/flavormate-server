/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.token.dtos.mappers

import de.flavormate.features.token.daos.TokenEntity
import de.flavormate.features.token.dtos.models.TokenDto
import de.flavormate.shared.interfaces.BasicMapper

object TokenDtoTokenEntityMapper : BasicMapper<TokenEntity, TokenDto>() {
  override fun mapNotNullBasic(input: TokenEntity): TokenDto {
    return TokenDto(
      id = input.id,
      createdAt = input.issuedAt,
      expiresAt = input.expiredAt,
      revoked = input.revoked,
      type = input.type,
      resource = input.securedResource,
    )
  }
}
