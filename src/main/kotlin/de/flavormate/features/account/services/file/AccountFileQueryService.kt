/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.services.file

import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.repositories.AccountFileRepository
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.enums.ImageSquareResolution
import de.flavormate.shared.services.FileService
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.core.StreamingOutput

@RequestScoped
class AccountFileQueryService(
  private val repository: AccountFileRepository,
  private val fileService: FileService,
) {

  fun getAccountsIdFile(id: String, resolution: ImageSquareResolution): StreamingOutput {
    val avatar =
      repository.findByOwnedById(id = id) ?: throw FNotFoundException(message = "Avatar not found")

    return fileService.streamFile(
      prefix = FilePath.AccountAvatar,
      uuid = avatar.id,
      fileName = resolution.fileName,
    )
  }
}
