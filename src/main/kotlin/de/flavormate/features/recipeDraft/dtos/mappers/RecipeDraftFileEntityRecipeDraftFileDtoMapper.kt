/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.dtos.mappers

import de.flavormate.features.recipeDraft.controllers.RecipeDraftController
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftFileEntity
import de.flavormate.features.recipeDraft.dtos.models.RecipeDraftFileDto
import de.flavormate.shared.interfaces.BasicMapper
import jakarta.ws.rs.core.UriBuilder

object RecipeDraftFileEntityRecipeDraftFileDtoMapper :
  BasicMapper<RecipeDraftFileEntity, RecipeDraftFileDto>() {
  override fun mapNotNullBasic(input: RecipeDraftFileEntity): RecipeDraftFileDto =
    RecipeDraftFileDto(
      id = input.id,
      path =
        UriBuilder.fromResource(RecipeDraftController::class.java)
          .path(
            RecipeDraftController::class.java,
            RecipeDraftController::getRecipeDraftsIdFilesFile.name,
          )
          .build(input.recipeDraft.id, input.id)
          .toString(),
    )
}
