/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.services.file

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.recipeDraft.dtos.mappers.RecipeDraftFileEntityRecipeDraftFileDtoMapper
import de.flavormate.features.recipeDraft.dtos.models.RecipeDraftFileDto
import de.flavormate.features.recipeDraft.repositories.RecipeDraftFileRepository
import de.flavormate.features.recipeDraft.repositories.RecipeDraftRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.enums.ImageResolution
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.FileService
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.core.StreamingOutput

@RequestScoped
class RecipeDraftFileQueryService(
  private val authorizationDetails: AuthorizationDetails,
  private val draftRepository: RecipeDraftRepository,
  private val fileRepository: RecipeDraftFileRepository,
  private val fileService: FileService,
) {

  // GET
  fun getRecipeDraftsIdFilesFile(
    id: String,
    file: String,
    resolution: ImageResolution,
  ): StreamingOutput {
    val draftEntity =
      draftRepository.findById(id = id) ?: throw FNotFoundException(message = "Recipe not found")

    if (!authorizationDetails.isAdminOrOwner(draftEntity))
      throw FForbiddenException(message = "You are not allowed to access this recipe draft!")

    val fileEntity =
      fileRepository.findById(id = file) ?: throw FNotFoundException(message = "File not found")

    return fileService.streamFile(
      prefix = FilePath.RecipeDraft,
      uuid = fileEntity.id,
      fileName = resolution.path,
    )
  }

  fun getRecipeDraftsIdFiles(id: String, pagination: Pagination): PageableDto<RecipeDraftFileDto> {
    val draftEntity =
      draftRepository.findById(id = id) ?: throw FNotFoundException(message = "Recipe not found")

    if (!authorizationDetails.isAdminOrOwner(draftEntity))
      throw FForbiddenException(message = "You are not allowed to access this recipe draft!")

    val dataQuery =
      fileRepository.findByRecipeDraft(id = id, sort = pagination.sortRequest(AllowedSorts.files))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = RecipeDraftFileEntityRecipeDraftFileDtoMapper::mapNotNullBasic,
    )
  }
}
