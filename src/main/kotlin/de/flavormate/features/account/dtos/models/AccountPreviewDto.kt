/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.dtos.models

data class AccountPreviewDto(
  override val id: String,
  override val displayName: String,
  override val avatar: AccountFileDto? = null,
) : AccountDto
