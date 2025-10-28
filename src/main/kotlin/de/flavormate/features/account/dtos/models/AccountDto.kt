/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.dtos.models

interface AccountDto {
  val id: String
  val displayName: String
  val avatar: AccountFileDto?
}
