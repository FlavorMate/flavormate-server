/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.account.dtos.models

interface AccountDto {
  val id: String
  val displayName: String
  val username: String
  val avatar: AccountFileDto?
}
