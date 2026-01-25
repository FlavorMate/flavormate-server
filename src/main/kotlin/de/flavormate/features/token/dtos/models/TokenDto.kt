/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.token.dtos.models

import de.flavormate.features.token.enums.TokenType
import java.time.LocalDateTime

data class TokenDto(
  val id: String,
  val createdAt: LocalDateTime,
  val expiresAt: LocalDateTime?,
  val revoked: Boolean,
  val type: TokenType,
  val resource: String,
) {}
