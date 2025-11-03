/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.dtos.models

import de.flavormate.shared.enums.Diet
import java.time.LocalDateTime

data class AccountFullDto(
  override val id: String,
  override val displayName: String,
  override val avatar: AccountFileDto? = null,
  val username: String,
  val diet: Diet,
  val email: String,
  val enabled: Boolean,
  val verified: Boolean,
  val firstLogin: Boolean,
  val createdOn: LocalDateTime,
  val roles: List<String>,
) : AccountDto
