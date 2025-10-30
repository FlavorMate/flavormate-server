/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.cron

import de.flavormate.core.auth.config.JwtBlockList
import de.flavormate.features.token.repositories.TokenRepository
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class TokenCron(val jwtBlockList: JwtBlockList, val tokenRepository: TokenRepository) {
  @Startup
  fun run() {
    Log.info("Reading all revoked keys from database")

    val tokens = tokenRepository.findAllRevoked()

    jwtBlockList.addAll(tokens)

    Log.info("Finished reading all revoked keys from database (${tokens.size})")
  }
}
