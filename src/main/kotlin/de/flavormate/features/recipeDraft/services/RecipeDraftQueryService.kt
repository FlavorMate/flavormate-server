/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.services

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.recipeDraft.dtos.mappers.RecipeDraftDtoFullMapper
import de.flavormate.features.recipeDraft.dtos.mappers.RecipeDraftDtoPreviewMapper
import de.flavormate.features.recipeDraft.dtos.models.RecipeDraftDtoFull
import de.flavormate.features.recipeDraft.dtos.models.RecipeDraftDtoPreview
import de.flavormate.features.recipeDraft.repositories.RecipeDraftRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class RecipeDraftQueryService(
  private val authorizationDetails: AuthorizationDetails,
  private val repository: RecipeDraftRepository,
) {

  // GET
  fun getRecipeDrafts(pagination: Pagination): PageableDto<RecipeDraftDtoPreview> {
    val self = authorizationDetails.getSelf()
    val dataQuery =
      repository.findAllByOwnedBy(
        id = self.id,
        sort = pagination.sortRequest(map = AllowedSorts.recipeDrafts),
      )

    val countQuery = repository.countAllByOwnedBy(id = self.id)

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      countQuery = countQuery,
      page = pagination.pageRequest,
      mapper = RecipeDraftDtoPreviewMapper::mapNotNullBasic,
    )
  }

  fun getRecipeDraftsId(id: String, language: String): RecipeDraftDtoFull {
    val entity =
      repository.findById(id)
        ?: throw FNotFoundException(message = "Recipe draft with id $id not found")

    if (!authorizationDetails.isAdminOrOwner(entity))
      throw FForbiddenException(message = "You are not allowed to access this recipe draft!")

    return RecipeDraftDtoFullMapper.mapNotNullL10n(input = entity, language = language)
  }
}
