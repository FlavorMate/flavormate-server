/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.auth.config

import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class JwtBlockList {

  private val blockedTokens: MutableList<String> = mutableListOf()

  fun add(jwt: String) {
    blockedTokens.add(jwt)
  }

  fun contains(jwt: String): Boolean {
    return blockedTokens.contains(jwt)
  }

  fun addAll(tokens: List<String>) {
    blockedTokens.clear()
    blockedTokens.addAll(tokens)
  }
}
