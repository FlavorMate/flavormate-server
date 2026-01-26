/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.utils

import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.extensions.mapToSet
import io.smallrye.jwt.build.Jwt
import java.security.MessageDigest
import java.time.Duration

object JwtUtils {
  fun generateToken(
    issuer: String,
    accountId: String,
    roles: Set<RoleTypes>,
    duration: Duration?,
  ): String {
    val tokenBuilder = Jwt.issuer(issuer).subject(accountId).groups(roles.mapToSet { it.name })

    duration?.also { tokenBuilder.expiresIn(it) }

    return tokenBuilder.sign()
  }

  fun hashJWT(token: String): String {
    return MessageDigest.getInstance("SHA-256").digest(token.toByteArray()).joinToString("") {
      "%02x".format(it)
    }
  }
}
