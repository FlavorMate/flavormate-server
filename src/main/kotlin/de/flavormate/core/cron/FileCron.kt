/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.cron

import de.flavormate.features.account.dao.models.AccountFileEntity
import de.flavormate.features.account.repositories.AccountFileRepository
import de.flavormate.features.recipe.daos.models.RecipeFileEntity
import de.flavormate.features.recipe.repositories.RecipeFileRepository
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftFileEntity
import de.flavormate.features.recipeDraft.repositories.RecipeDraftFileRepository
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.enums.ImageResolution
import de.flavormate.shared.interfaces.CRepository
import de.flavormate.shared.services.FileService
import de.flavormate.utils.DatabaseUtils
import de.flavormate.utils.ImageUtils
import de.flavormate.utils.MimeTypes
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*
import org.apache.commons.io.FileUtils

@ApplicationScoped
class FileCron(
  private val fileAccountRepository: AccountFileRepository,
  private val fileService: FileService,
  private val fileRecipeRepository: RecipeFileRepository,
  private val fileRecipeDraftRepository: RecipeDraftFileRepository,
) {

  @Scheduled(cron = "5 0 0 * * ?")
  @Startup
  fun run() {
    Log.info("Start deleting empty or orphan folders")

    cleanCategory(FilePath.AccountAvatar, fileAccountRepository)

    cleanCategory(FilePath.Recipe, fileRecipeRepository)

    cleanCategory(FilePath.RecipeDraft, fileRecipeDraftRepository)

    Log.info("Finished deleting empty or orphan folders")

    Log.info("Start deleting missing db entries")

    cleanAvatarTable(FilePath.AccountAvatar, fileAccountRepository)

    Log.info("Finished deleting missing db entries")

    Log.info("Start generating missing thumbnails")

    createMissingThumbnails()

    Log.info("Finished generating missing thumbnails")
  }

  @Transactional
  fun cleanCategory(prefix: FilePath, repository: CRepository<*>) {
    val path = fileService.rootPath(prefix)
    path.createDirectories()

    executeFolder(path, 0) { segment1 ->
      executeFolder(segment1, 1) { segment2 ->
        executeFolder(segment2, 2) { recipe ->
          val exists = repository.existsById(recipe.fileName.toString())
          if (!exists) {
            deleteFolder(recipe)
          }
        }
      }
    }

    for (id in repository.findIds()) {
      if (!fileService.readPath(prefix, id).exists()) {
        repository.deleteById(id)
      }
    }
  }

  fun executeFolder(folder: Path, level: Int, callback: (Path) -> Unit) {
    var subFolders = Files.list(folder).filter(Files::isDirectory).toList()

    subFolders.forEach { callback(it) }

    if (level == 0) return

    subFolders = Files.list(folder).filter(Files::isDirectory).toList()

    if (subFolders.isEmpty()) {
      deleteFolder(folder)
    }
  }

  fun deleteFolder(folder: Path) {
    Log.info("Deleting empty folder: ${folder.absolutePathString()}")
    FileUtils.deleteDirectory(folder.toFile())
  }

  @Transactional
  fun cleanAvatarTable(prefix: FilePath, repository: AccountFileRepository) {
    DatabaseUtils.batchedRun(query = repository.findAll()) { items, _ ->
      items.forEach { data ->
        // Check filesystem existence outside the main logic if possible
        val path = fileService.readPath(prefix, data.id)
        val isNonExistent =
          !path.exists() || Files.list(path).use { stream -> stream.findAny().isEmpty }

        if (data.ownedBy.avatar?.id != data.id) {
          Log.info("Deleting orphan db entry: ${data.id}")
          repository.deleteById(data.id)
        } else if (isNonExistent) {
          Log.info("Deleting entry with non-existent files: ${data.id}")
          data.ownedBy.avatar = null
          repository.deleteById(data.id)
        }
      }
    }
  }

  fun createMissingThumbnails() {
    var count = fileAccountRepository.findBySchema(1).count()
    DatabaseUtils.batchedRunSingle(query = fileAccountRepository.findBySchema(1)) {
      item,
      currentIndex ->
      Log.info("Creating missing account thumbnails ($currentIndex/$count)")
      createMissingAccountThumbnail(item)
    }

    count = fileRecipeRepository.findBySchema(1).count()
    DatabaseUtils.batchedRunSingle(query = fileRecipeRepository.findBySchema(1)) {
      item,
      currentIndex ->
      Log.info("Creating missing recipe thumbnails ($currentIndex/$count)")
      createMissingRecipeThumbnail(item)
    }

    count = fileRecipeDraftRepository.findBySchema(1).count()
    DatabaseUtils.batchedRunSingle(query = fileRecipeDraftRepository.findBySchema(1)) {
      item,
      currentIndex ->
      Log.info("Creating missing recipe draft thumbnails ($currentIndex/$count)")
      createMissingRecipeDraftThumbnail(item)
    }
  }

  fun createMissingAccountThumbnail(item: AccountFileEntity) {
    val file = fileAccountRepository.findById(item.id) ?: return
    val path = fileService.readPath(prefix = FilePath.AccountAvatar, uuid = file.id)
    val inputFile = path.resolve("Original${MimeTypes.WEBP_EXTENSION}")

    if (file.schema < 2) {
      ImageUtils.createPlaneImage(inputFile = inputFile, outputDir = path, newFile = true)
      path
        .listDirectoryEntries()
        .filter { it.isRegularFile() && it.name != ImageResolution.Original.path.name }
        .forEach { it.deleteExisting() }
      file.schema = 2
    }
  }

  fun createMissingRecipeThumbnail(item: RecipeFileEntity) {
    val file = fileRecipeRepository.findById(item.id) ?: return
    val path = fileService.readPath(prefix = FilePath.Recipe, uuid = file.id)
    val inputFile = path.resolve(ImageResolution.Original.path)

    if (file.schema < 2) {
      ImageUtils.createDynamicImage(inputFile = inputFile, outputDir = path, newFile = false)
      path
        .listDirectoryEntries()
        .filter { it.isRegularFile() && it.name != ImageResolution.Original.path.name }
        .forEach { it.deleteExisting() }
      file.schema = 2
    }
  }

  fun createMissingRecipeDraftThumbnail(item: RecipeDraftFileEntity) {
    val file = fileRecipeDraftRepository.findById(item.id) ?: return
    val path = fileService.readPath(prefix = FilePath.RecipeDraft, uuid = file.id)
    val inputFile = path.resolve(ImageResolution.Original.path)

    if (file.schema < 2) {
      ImageUtils.createDynamicImage(inputFile = inputFile, outputDir = path, newFile = false)
      path
        .listDirectoryEntries()
        .filter { it.isRegularFile() && it.name != ImageResolution.Original.path.name }
        .forEach { it.deleteExisting() }
      file.schema = 2
    }
  }
}
