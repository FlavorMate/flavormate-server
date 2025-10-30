/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.token.enums

enum class TokenType {
  ACCOUNT,
  BRING,
  PASSWORD,
  REFRESH,
  RESET,
  SHARE,
  VERIFY;

  companion object {
    const val ACCOUNT_VALUE = "ACCOUNT"
    const val BRING_VALUE = "BRING"
    const val PASSWORD_VALUE = "PASSWORD"
    const val REFRESH_VALUE = "REFRESH"
    const val RESET_VALUE = "RESET"
    const val SHARE_VALUE = "SHARE"
    const val VERIFY_VALUE = "VERIFY"
  }
}
