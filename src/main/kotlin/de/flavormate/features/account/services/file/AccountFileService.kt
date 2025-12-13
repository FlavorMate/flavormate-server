/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.services.file

import de.flavormate.shared.enums.ImageResolution
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import java.io.File

@RequestScoped
class AccountFileService(
  private val mutationService: AccountFileMutationService,
  private val queryService: AccountFileQueryService,
) {
  // GET
  fun getAccountsIdFile(id: String, resolution: ImageResolution) =
    queryService.getAccountsIdFile(id = id, resolution = resolution)

  // POST
  @Transactional
  fun createAccountsIdAvatar(file: File, id: String) =
    mutationService.createAccountsIdFile(file = file, id = id)

  // DELETE
  @Transactional
  fun deleteAccountsIdAvatar(id: String) = mutationService.deleteAccountsIdFile(id = id)
}
