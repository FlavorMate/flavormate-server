/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.cron

import de.flavormate.features.recipeDraft.repositories.RecipeDraftFileRepository
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.enums.ImageResolution
import de.flavormate.shared.services.FileService
import de.flavormate.utils.ImageUtils
import io.quarkus.logging.Log
import io.quarkus.narayana.jta.QuarkusTransaction
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.io.path.deleteIfExists
import kotlin.io.path.name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

@ApplicationScoped
class ImageCron(
  private val recipeDraftFileRepository: RecipeDraftFileRepository,
  private val fileService: FileService,
) {
  private val running = AtomicBoolean(false)

  @Scheduled(cron = "*/15 * * * * ?")
  fun generateThumbnails() {
    if (running.get()) return

    running.set(true)

    val imagesToGenerate = recipeDraftFileRepository.findAllTemporary(limit = 10)

    runBlocking(Dispatchers.IO) {
      imagesToGenerate
        .map { image ->
          async {
            runCatching {
                Log.debug("Generating thumbnails for image: $image")

                val outputPath = fileService.readPath(FilePath.RecipeDraft, image)
                val temporaryImage = outputPath.resolve(ImageResolution.Temporary.fileName.name)

                ImageUtils.createDynamicImage(temporaryImage, outputPath)

                QuarkusTransaction.requiringNew().run {
                  recipeDraftFileRepository.updateDeleteTemporary(image)
                }

                temporaryImage.deleteIfExists()
              }
              .getOrNull()
          }
        }
        .awaitAll()
    }

    running.set(false)
  }
}
