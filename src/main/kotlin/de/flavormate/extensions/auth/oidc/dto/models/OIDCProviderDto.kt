/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.dto.models

data class OIDCProviderDto(
  val url: String,
  val id: String,
  val issuer: String,
  val clientId: String,
  val label: String,
  val redirectUri: String,
  val icon: ByteArray? = null,
)
