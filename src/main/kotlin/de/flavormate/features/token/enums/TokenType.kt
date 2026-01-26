/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.token.enums

enum class TokenType {
  BRING,
  PASSWORD,
  RESET,
  SHARE,
  VERIFY;

  companion object {
    const val BRING_VALUE = "BRING"
    const val PASSWORD_VALUE = "PASSWORD"
    const val RESET_VALUE = "RESET"
    const val SHARE_VALUE = "SHARE"
    const val VERIFY_VALUE = "VERIFY"
  }
}
