/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.auth.models

data class TokenResponseDao(
  val access_token: String,
  val token_type: String,
  val expires_in: Long,
  val refresh_token: String,
) {
  companion object {
    fun create(accessToken: String, expiresIn: Long, refreshToken: String) =
      TokenResponseDao(accessToken, "Bearer", expiresIn, refreshToken)
  }
}
