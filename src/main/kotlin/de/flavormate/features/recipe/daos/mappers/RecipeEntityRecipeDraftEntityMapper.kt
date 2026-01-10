/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.daos.mappers

import de.flavormate.features.recipe.daos.mappers.ingredient.IngredientGroupEntityIngredientGroupDraftEntityMapper
import de.flavormate.features.recipe.daos.mappers.instruction.InstructionGroupEntityInstructionGroupDraftEntityMapper
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.shared.extensions.mapToSet
import de.flavormate.shared.interfaces.BasicMapper

object RecipeEntityRecipeDraftEntityMapper : BasicMapper<RecipeDraftEntity, RecipeEntity>() {
  override fun mapNotNullBasic(input: RecipeDraftEntity): RecipeEntity {
    return RecipeEntity().apply {
      this.ownedBy = input.ownedBy
      this.ownedById = input.ownedById
      this.label = input.label!!
      this.description = input.description
      this.prepTime = input.prepTime
      this.cookTime = input.cookTime
      this.restTime = input.restTime
      this.serving = ServingEntityServingDraftEntityMapper.mapNotNullBasic(input.serving)
      this.instructionGroups =
        input.instructionGroups
          .mapToSet {
            InstructionGroupEntityInstructionGroupDraftEntityMapper.mapNotNullBasic(it).also {
              it.recipe = this
            }
          }!!
          .toMutableList()
      this.ingredientGroups =
        input.ingredientGroups
          .mapToSet {
            IngredientGroupEntityIngredientGroupDraftEntityMapper.mapNotNullBasic(it).also {
              it.recipe = this
            }
          }!!
          .toMutableList()
      this.course = input.course!!
      this.diet = input.diet!!
      this.url = input.url
    }
  }
}
