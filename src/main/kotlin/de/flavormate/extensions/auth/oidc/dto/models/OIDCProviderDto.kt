/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.dto.models

data class OIDCProviderDto(
  val url: String,
  val clientId: String,
  val name: String,
  val id: String,
  val redirectUri: String,
  val iconPath: String?,
)
