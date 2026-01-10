/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.services.file

import de.flavormate.shared.enums.ImageResolution
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import java.io.File

@RequestScoped
class RecipeDraftFileService(
  private val mutationService: RecipeDraftFileMutationService,
  private val queryService: RecipeDraftFileQueryService,
) {

  // Queries
  fun getRecipeDraftsIdFiles(id: String, pagination: Pagination) =
    queryService.getRecipeDraftsIdFiles(id = id, pagination = pagination)

  fun getRecipeDraftsIdFilesFile(id: String, file: String, resolution: ImageResolution) =
    queryService.getRecipeDraftsIdFilesFile(id = id, file = file, resolution = resolution)

  // Mutations
  @Transactional
  fun deleteRecipeDraftsIdFilesFile(id: String, file: String) =
    mutationService.deleteRecipeDraftsIdFilesFile(id = id, file = file)

  @Transactional
  fun createRecipeDraftsIdFilesFile(id: String, file: File) =
    mutationService.createRecipeDraftsIdFilesFile(id = id, file = file)
}
