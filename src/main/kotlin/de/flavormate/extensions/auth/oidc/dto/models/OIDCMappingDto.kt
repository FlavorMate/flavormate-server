/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.dto.models

import java.time.LocalDateTime

data class OIDCMappingDto(
  val issuer: String,
  val subject: String,
  val name: String,
  val createdOn: LocalDateTime,
  val providerId: String,
  val providerName: String,
  val icon: ByteArray? = null,
)
