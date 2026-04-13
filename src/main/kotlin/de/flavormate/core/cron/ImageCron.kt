/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.cron

import de.flavormate.features.recipe.repositories.RecipeFileRepository
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
import kotlin.io.path.name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

@ApplicationScoped
class ImageCron(
  private val recipeDraftFileRepository: RecipeDraftFileRepository,
  private val recipeFileRepository: RecipeFileRepository,
  private val fileService: FileService,
) {
  private val running = AtomicBoolean(false)

  @Scheduled(cron = "*/15 * * * * ?")
  fun generateThumbnails() {
    if (running.get()) return

    running.set(true)

    val recipeDraftsWithMissingThumbnails = recipeDraftFileRepository.findAllTemporary(limit = 10)
    val recipesWithMissingThumbnails = recipeFileRepository.findAllTemporary(limit = 10)

    runBlocking(Dispatchers.IO) {
      recipeDraftsWithMissingThumbnails
        .map { image ->
          async {
            runCatching {
                Log.debug("Generating thumbnails for image: $image")

                val outputPath = fileService.readPath(FilePath.RecipeDraft, image)
                val originalImage = outputPath.resolve(ImageResolution.Original.fileName.name)

                ImageUtils.createDynamicImage(originalImage, outputPath, newFile = false)

                QuarkusTransaction.requiringNew().run {
                  recipeDraftFileRepository.updateDeleteTemporary(image)
                }
              }
              .getOrNull()
          }
        }
        .awaitAll()
    }

    runBlocking(Dispatchers.IO) {
      recipesWithMissingThumbnails
        .map { image ->
          async {
            runCatching {
                Log.debug("Generating thumbnails for image: $image")

                val outputPath = fileService.readPath(FilePath.Recipe, image)
                val originalImage = outputPath.resolve(ImageResolution.Original.fileName.name)

                ImageUtils.createDynamicImage(originalImage, outputPath, newFile = false)

                QuarkusTransaction.requiringNew().run {
                  recipeFileRepository.updateDeleteTemporary(image)
                }
              }
              .getOrNull()
          }
        }
        .awaitAll()
    }

    running.set(false)
  }
}
