/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.account.services.file

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.dao.models.AccountFileEntity
import de.flavormate.features.account.repositories.AccountFileRepository
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.FileService
import de.flavormate.shared.services.TransactionService
import de.flavormate.utils.ImageUtils
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import java.io.File
import java.nio.file.Paths

@RequestScoped
class AccountFileMutationService(
  private val accountRepository: AccountRepository,
  private val authorizationDetails: AuthorizationDetails,
  private val avatarRepository: AccountFileRepository,
  private val fileService: FileService,
  private val transactionService: TransactionService,
) {
  // DELETE
  @Transactional
  fun deleteAccountsIdFile(id: String): Boolean {
    transactionService.initialize()

    val account =
      accountRepository.findById(id) ?: throw FNotFoundException(message = "Account not found")

    if (!authorizationDetails.isAdminOrOwner(target = account))
      throw FForbiddenException(message = "You are not allowed to delete this account!")

    transactionService.pendingOperations.add {
      fileService.deleteFolder(prefix = FilePath.AccountAvatar, uuid = account.avatar!!.id)
    }

    return avatarRepository.deleteByOwnedById(id)
  }

  // POST
  @Transactional
  fun createAccountsIdFile(file: File, id: String) {
    transactionService.initialize()

    val account =
      accountRepository.findById(id) ?: throw FNotFoundException(message = "Account not found")

    if (!authorizationDetails.isAdminOrOwner(target = account))
      throw FForbiddenException(
        message = "You are not allowed to upload an avatar for this account!"
      )

    // remove the old avatar
    if (account.avatar != null) {
      val uuid = account.avatar!!.id
      transactionService.pendingOperations.add {
        fileService.deleteFolder(prefix = FilePath.AccountAvatar, uuid = uuid)
      }

      avatarRepository.deleteByOwnedById(id = account.id)
    }

    val avatarEntity = AccountFileEntity.create(account = account).also(avatarRepository::persist)

    account.avatar = avatarEntity
    accountRepository.persist(account)

    val inputFile = Paths.get(file.path)
    val outputDir = fileService.createPath(prefix = FilePath.AccountAvatar, uuid = avatarEntity.id)

    ImageUtils.createPlaneImage(inputFile = inputFile, outputDir = outputDir)
  }
}
