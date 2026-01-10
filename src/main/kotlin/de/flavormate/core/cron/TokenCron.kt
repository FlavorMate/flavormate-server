/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.cron

import de.flavormate.core.auth.config.JwtBlockList
import de.flavormate.features.token.repositories.TokenRepository
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class TokenCron(val jwtBlockList: JwtBlockList, val tokenRepository: TokenRepository) {

  @Startup
  @Transactional
  @Scheduled(cron = "0 0 0 * * ?")
  fun run() {
    Log.info("Removing all expired keys from database")

    val removedTokens = tokenRepository.deleteAllRevokedAndExpired()

    Log.info("Removed $removedTokens expired keys from database")

    Log.info("Reading all revoked keys from database")

    val tokens = tokenRepository.findAllRevoked()

    jwtBlockList.addAll(tokens)

    Log.info("Finished reading all revoked keys from database (${tokens.size})")
  }
}
