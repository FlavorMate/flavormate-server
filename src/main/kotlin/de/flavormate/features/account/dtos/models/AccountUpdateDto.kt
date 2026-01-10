/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.account.dtos.models

import de.flavormate.shared.enums.Diet

data class AccountUpdateDto(
  val diet: Diet?,
  val email: String?,
  val oldPassword: String?,
  val newPassword: String?,
)
