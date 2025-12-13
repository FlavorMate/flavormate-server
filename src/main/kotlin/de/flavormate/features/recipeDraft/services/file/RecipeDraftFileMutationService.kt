/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.services.file

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftFileEntity
import de.flavormate.features.recipeDraft.repositories.RecipeDraftFileRepository
import de.flavormate.features.recipeDraft.repositories.RecipeDraftRepository
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.FileService
import de.flavormate.shared.services.TransactionService
import de.flavormate.utils.ImageUtils
import jakarta.enterprise.context.RequestScoped
import java.io.File
import java.nio.file.Paths

@RequestScoped
class RecipeDraftFileMutationService(
  private val authorizationDetails: AuthorizationDetails,
  private val draftRepository: RecipeDraftRepository,
  private val fileRepository: RecipeDraftFileRepository,
  private val fileService: FileService,
  private val transactionService: TransactionService,
) {

  fun deleteRecipeDraftsIdFilesFile(id: String, file: String): Boolean {
    transactionService.initialize()

    val draftEntity =
      draftRepository.findById(id = id)
        ?: throw FNotFoundException(message = "Recipe draft not found!")

    if (!authorizationDetails.isAdminOrOwner(target = draftEntity))
      throw FForbiddenException(message = "You are not allowed to delete files!")

    fileRepository.findById(id = file) ?: throw FNotFoundException(message = "File not found!")

    transactionService.pendingOperations.add {
      fileService.deleteFolder(prefix = FilePath.RecipeDraft, uuid = file)
    }

    draftEntity.files.removeIf { it.id == file }

    // remove the old image from the db
    return fileRepository.deleteById(id)
  }

  fun createRecipeDraftsIdFilesFile(id: String, file: File) {
    val self = authorizationDetails.getSelf()

    val recipeDraft =
      draftRepository.findById(id) ?: throw FNotFoundException(message = "Recipe draft not found!")

    if (!authorizationDetails.isAdminOrOwner(recipeDraft))
      throw FForbiddenException(message = "You are not allowed to upload files!")

    val fileEntity =
      RecipeDraftFileEntity.create(account = self, recipeDraft = recipeDraft).also {
        fileRepository.persist(it)
      }

    val path = fileService.createPath(prefix = FilePath.RecipeDraft, uuid = fileEntity.id)

    val inputFile = Paths.get(file.path)

    ImageUtils.createDynamicImage(inputFile = inputFile, outputDir = path)
  }
}
