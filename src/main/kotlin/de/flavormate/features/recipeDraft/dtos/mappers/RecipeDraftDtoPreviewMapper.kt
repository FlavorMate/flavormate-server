/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.dtos.mappers

import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.features.recipeDraft.dtos.models.RecipeDraftDtoPreview
import de.flavormate.shared.interfaces.BasicMapper

object RecipeDraftDtoPreviewMapper : BasicMapper<RecipeDraftEntity, RecipeDraftDtoPreview>() {
  override fun mapNotNullBasic(input: RecipeDraftEntity) =
    RecipeDraftDtoPreview(
      id = input.id,
      version = input.version,
      createdOn = input.createdOn,
      lastModifiedOn = input.lastModifiedOn,
      label = input.label,
      originId = input.originId,
    )
}
