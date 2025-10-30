/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.daos.mappers.ingredient

import de.flavormate.features.recipe.daos.models.ingredient.IngredientGroupEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupEntity
import de.flavormate.shared.extensions.mapToSet
import de.flavormate.shared.interfaces.BasicMapper

object IngredientGroupEntityIngredientGroupDraftEntityMapper :
  BasicMapper<RecipeDraftIngredientGroupEntity, IngredientGroupEntity>() {
  override fun mapNotNullBasic(input: RecipeDraftIngredientGroupEntity): IngredientGroupEntity {
    return IngredientGroupEntity().apply {
      this.label = input.label
      this.index = input.index
      this.ingredients =
        input.ingredients.mapToSet {
          IngredientEntityIngredientDraftEntityMapper.mapNotNullBasic(it).also { it.group = this }
        }!!
    }
  }
}
