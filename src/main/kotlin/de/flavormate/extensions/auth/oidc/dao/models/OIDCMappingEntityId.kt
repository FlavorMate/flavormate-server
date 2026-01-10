/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.dao.models

import java.util.*

class OIDCMappingEntityId {
  lateinit var issuer: String

  lateinit var subject: String

  lateinit var accountId: String

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null) return false
    if (javaClass != other.javaClass) return false
    return Objects.hash(issuer, subject, accountId) == other.hashCode()
  }

  override fun hashCode(): Int {
    return Objects.hash(issuer, subject, accountId)
  }
}
