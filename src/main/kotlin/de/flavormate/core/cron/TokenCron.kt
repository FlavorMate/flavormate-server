/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.cron

import de.flavormate.features.token.repositories.TokenRepository
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class TokenCron(val tokenRepository: TokenRepository) {

  @Startup
  @Transactional
  @Scheduled(cron = "0 0 0 * * ?")
  fun run() {
    Log.info("Removing all expired keys from database")

    val removedTokens = tokenRepository.deleteAllExpired()

    Log.info("Removed $removedTokens expired keys from database")
  }
}
