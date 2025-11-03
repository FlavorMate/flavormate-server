/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.admin.dto

data class AccountForm(
  val displayName: String,
  val username: String,
  val password: String,
  val email: String,
)
