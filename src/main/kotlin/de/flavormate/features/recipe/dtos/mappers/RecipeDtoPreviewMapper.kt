/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.dtos.mappers

import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipe.dtos.models.RecipeDtoPreview
import de.flavormate.shared.interfaces.BasicMapper

object RecipeDtoPreviewMapper : BasicMapper<RecipeEntity, RecipeDtoPreview>() {
  override fun mapNotNullBasic(input: RecipeEntity): RecipeDtoPreview {
    return RecipeDtoPreview(
      id = input.id,
      createdOn = input.createdOn,
      label = input.label,
      diet = input.diet,
      cookTime = input.cookTime,
      prepTime = input.prepTime,
      restTime = input.restTime,
      cover = input.coverFile?.let(RecipeFileDtoPreviewMapper::mapNotNullBasic),
    )
  }
}
