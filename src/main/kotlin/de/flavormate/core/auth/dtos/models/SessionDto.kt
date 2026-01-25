/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.dtos.models

import java.time.LocalDateTime

data class SessionDto(
  val id: String,
  val createdAt: LocalDateTime,
  val lastModifiedAt: LocalDateTime,
  val expiresAt: LocalDateTime,
  val revoked: Boolean,
  val userAgent: String?,
)
