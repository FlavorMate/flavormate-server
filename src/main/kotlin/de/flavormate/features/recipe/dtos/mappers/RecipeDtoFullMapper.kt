/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.dtos.mappers

import de.flavormate.features.account.dtos.mappers.AccountPreviewDtoMapper
import de.flavormate.features.category.dtos.mappers.CategoryDtoMapper
import de.flavormate.features.recipe.daos.models.NutritionEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipe.daos.models.ServingEntity
import de.flavormate.features.recipe.daos.models.ingredient.IngredientEntity
import de.flavormate.features.recipe.daos.models.ingredient.IngredientGroupEntity
import de.flavormate.features.recipe.daos.models.instruction.InstructionEntity
import de.flavormate.features.recipe.daos.models.instruction.InstructionGroupEntity
import de.flavormate.features.recipe.dtos.models.*
import de.flavormate.features.tag.dtos.mappers.TagDtoMapper
import de.flavormate.features.unit.dtos.mappers.UnitLocalizedDtoMapper
import de.flavormate.shared.interfaces.L10nMapper

object RecipeDtoFullMapper : L10nMapper<RecipeEntity, RecipeDtoFull>() {
  override fun mapNotNullL10n(input: RecipeEntity, language: String): RecipeDtoFull {
    return RecipeDtoFull(
      id = input.id,
      createdOn = input.createdOn,
      version = input.version,
      ownedBy = AccountPreviewDtoMapper.mapNotNullBasic(input.ownedBy),
      label = input.label,
      description = input.description,
      prepTime = input.prepTime,
      cookTime = input.cookTime,
      restTime = input.restTime,
      serving = mapServing(input.serving),
      instructionGroups = input.instructionGroups.map(::mapInstructionGroup),
      ingredientGroups = input.ingredientGroups.map(::mapIngredientGroup),
      course = input.course,
      diet = input.diet,
      url = input.url,
      cover = input.coverFile?.let(RecipeFileDtoPreviewMapper::mapNotNullBasic),
      categories =
        input.categories.map { CategoryDtoMapper.mapNotNullL10n(input = it, language = language) },
      files = input.files.take(8).map(RecipeFileDtoPreviewMapper::mapNotNullBasic),
      tags = input.tags.map(TagDtoMapper::mapNotNullBasic),
    )
  }

  private fun mapServing(input: ServingEntity) =
    RecipeServingDto(id = input.id, amount = input.amount, label = input.label)

  private fun mapInstructionGroup(input: InstructionGroupEntity) =
    RecipeInstructionGroupDto(
      id = input.id,
      index = input.index,
      instructions = input.instructions.map(::mapInstructionGroupItem),
      label = input.label,
    )

  private fun mapInstructionGroupItem(input: InstructionEntity) =
    RecipeInstructionGroupItemDto(id = input.id, index = input.index, label = input.label)

  private fun mapIngredientGroup(input: IngredientGroupEntity) =
    RecipeIngredientGroupDto(
      id = input.id,
      index = input.index,
      ingredients = input.ingredients.map(::mapIngredientGroupItem),
      label = input.label,
    )

  private fun mapIngredientGroupItem(input: IngredientEntity) =
    RecipeIngredientGroupItemDto(
      id = input.id,
      label = input.label,
      index = input.index,
      amount = input.amount,
      unit = input.unit?.let(UnitLocalizedDtoMapper::mapNotNullBasic),
      nutrition = input.nutrition?.let(::mapNutrition),
    )

  private fun mapNutrition(input: NutritionEntity) =
    if (input.openFoodFactsId == null)
      RecipeIngredientGroupItemNutritionDto(
        id = input.id,
        openFoodFactsId = null,
        carbohydrates = input.carbohydrates,
        energyKcal = input.energyKcal,
        fat = input.fat,
        fiber = input.fiber,
        proteins = input.proteins,
        salt = input.salt,
        saturatedFat = input.saturatedFat,
        sodium = input.sodium,
        sugars = input.sugars,
      )
    else
      RecipeIngredientGroupItemNutritionDto(
        id = input.id,
        openFoodFactsId = input.openFoodFactsId!!.id,
        carbohydrates = input.openFoodFactsId!!.carbohydrates,
        energyKcal = input.openFoodFactsId!!.energyKcal,
        fat = input.openFoodFactsId!!.fat,
        fiber = input.openFoodFactsId!!.fiber,
        proteins = input.openFoodFactsId!!.proteins,
        salt = input.openFoodFactsId!!.salt,
        saturatedFat = input.openFoodFactsId!!.saturatedFat,
        sodium = input.openFoodFactsId!!.sodium,
        sugars = input.openFoodFactsId!!.sugars,
      )
}
