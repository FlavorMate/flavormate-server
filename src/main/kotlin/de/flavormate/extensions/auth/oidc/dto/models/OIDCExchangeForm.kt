/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.dto.models

data class OIDCExchangeForm(
  val issuer: String,
  val clientId: String,
  val code: String,
  val codeVerifier: String,
  val redirectUri: String,
)
