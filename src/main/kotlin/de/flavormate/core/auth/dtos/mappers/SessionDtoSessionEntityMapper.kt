/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.dtos.mappers

import de.flavormate.core.auth.dao.models.SessionEntity
import de.flavormate.core.auth.dtos.models.SessionDto
import de.flavormate.shared.interfaces.BasicMapper

object SessionDtoSessionEntityMapper : BasicMapper<SessionEntity, SessionDto>() {
  override fun mapNotNullBasic(input: SessionEntity): SessionDto {
    return SessionDto(
      id = input.id,
      createdAt = input.createdOn,
      lastModifiedAt = input.lastModifiedOn,
      expiresAt = input.expiresAt,
      revoked = input.revoked,
      userAgent = input.userAgent,
    )
  }
}
