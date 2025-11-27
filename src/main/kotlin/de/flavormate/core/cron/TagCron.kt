/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.cron

import de.flavormate.features.tag.repositories.TagRepository
import de.flavormate.shared.extensions.toKebabCase
import de.flavormate.utils.DatabaseUtils
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
    Log.info("Start tag cleanup")

    Log.info("Cleaning up orphan tags")
    cleanOrphanTags()
    Log.info("Finished cleaning up orphan tags")

    Log.info("Cleaning up tag labels")
    cleanTagLabels()
    Log.info("Finished cleaning up tag labels")

    Log.info("Finished tag cleanup")
  }

  @Transactional
  fun cleanOrphanTags() {
    val deletedTags = tagRepository.deleteOrphanTags()
    Log.info("Deleted $deletedTags orphaned tags")
  }

  @Transactional
  fun cleanTagLabels() {
    DatabaseUtils.batchedRun(query = tagRepository.findAll()) { items ->
      for (tag in items) {
        val originalLabel = tag.label
        val cleanedLabel = originalLabel.toKebabCase()

        if (originalLabel == cleanedLabel) continue

        tag.label = tag.label.toKebabCase()
        tagRepository.persist(tag)

        Log.info("Tag $originalLabel was unoptimized and was optimized: $cleanedLabel")
      }
    }
  }
}
