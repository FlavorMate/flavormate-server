/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.dto.models

data class OIDCConfigWrapper(
  val issuer: String,
  val clientId: String,
  val clientSecret: String?,
  val label: String,
  val iconPath: String?,
  val overrideRedirectUri: Boolean,
  val urlTokenEndpoint: String,
  val urlDiscoveryEndpoint: String,
)
