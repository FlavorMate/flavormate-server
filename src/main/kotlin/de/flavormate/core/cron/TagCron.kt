/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.cron

import de.flavormate.features.tag.repositories.TagRepository
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class TagCron(private val tagRepository: TagRepository) {

  @Scheduled(cron = "0 0 0 * * ?")
  @Startup
  @Transactional
  fun run() {
    val deletedTags = tagRepository.deleteOrphanTags()
    Log.info("Deleted $deletedTags orphaned tags")
  }
}
