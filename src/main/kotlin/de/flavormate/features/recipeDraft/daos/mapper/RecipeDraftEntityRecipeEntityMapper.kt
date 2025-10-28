/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.mapper

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.shared.interfaces.OwnedMapper

object RecipeDraftEntityRecipeEntityMapper : OwnedMapper<RecipeEntity, RecipeDraftEntity>() {
  override fun mapNotNullOwned(input: RecipeEntity, account: AccountEntity): RecipeDraftEntity {
    return RecipeDraftEntity.create(account).apply {
      this.label = input.label
      this.description = input.description
      this.prepTime = input.prepTime
      this.cookTime = input.cookTime
      this.restTime = input.restTime
      this.serving = ServingDraftEntityServingEntityMapper.mapNotNullBasic(input.serving)
      this.ingredientGroups =
        input.ingredientGroups.mapTo(mutableListOf()) { group ->
          IngredientGroupDraftEntityIngredientGroupEntityMapper.mapNotNullBasic(group).also {
            it.recipe = this
          }
        }
      this.instructionGroups =
        input.instructionGroups.mapTo(mutableListOf()) { group ->
          InstructionGroupDraftEntityInstructionGroupEntityMapper.mapNotNullBasic(group).also {
            it.recipe = this
          }
        }
      this.tags = input.tags.mapTo(mutableListOf()) { it.label }
      this.course = input.course
      this.diet = input.diet
      this.url = input.url
      this.originId = input.id
    }
  }
}
