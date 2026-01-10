/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.storyDraft.daos.mappers

import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.storyDraft.daos.models.StoryDraftRecipeEntity
import de.flavormate.shared.interfaces.BasicMapper

object StoryDraftRecipeEntityRecipeEntityMapper :
  BasicMapper<RecipeEntity, StoryDraftRecipeEntity>() {
  override fun mapNotNullBasic(input: RecipeEntity): StoryDraftRecipeEntity {
    return StoryDraftRecipeEntity().apply {
      this.id = input.id
      this.label = input.label
      this.cover = input.files.firstOrNull()?.id
    }
  }
}
