/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.registration.models

data class RegistrationForm(
  val username: String,
  val displayName: String,
  val password: String,
  val email: String,
)
