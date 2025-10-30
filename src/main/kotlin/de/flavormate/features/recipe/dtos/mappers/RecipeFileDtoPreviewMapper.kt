/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.dtos.mappers

import de.flavormate.features.recipe.controllers.RecipeController
import de.flavormate.features.recipe.daos.models.RecipeFileEntity
import de.flavormate.features.recipe.dtos.models.RecipeFileDtoPreview
import de.flavormate.shared.interfaces.BasicMapper
import jakarta.ws.rs.core.UriBuilder

object RecipeFileDtoPreviewMapper : BasicMapper<RecipeFileEntity, RecipeFileDtoPreview>() {
  override fun mapNotNullBasic(input: RecipeFileEntity): RecipeFileDtoPreview =
    RecipeFileDtoPreview(
      id = input.id,
      path =
        UriBuilder.fromResource(RecipeController::class.java)
          .path(RecipeController::class.java, RecipeController::getRecipeCover.name)
          .build(input.recipe.id)
          .toString(),
    )
}
