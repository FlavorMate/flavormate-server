/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.dtos.mappers

import de.flavormate.features.account.dtos.mappers.AccountPreviewDtoMapper
import de.flavormate.features.category.dtos.mappers.CategoryDtoMapper
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftIngredientGroupItemNutritionEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftServingEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupItemEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupItemEntity
import de.flavormate.features.recipeDraft.dtos.models.*
import de.flavormate.features.unit.dtos.mappers.UnitLocalizedDtoMapper
import de.flavormate.shared.interfaces.L10nMapper

object RecipeDraftDtoFullMapper : L10nMapper<RecipeDraftEntity, RecipeDraftDtoFull>() {
    override fun mapNotNullL10n(input: RecipeDraftEntity, language: String) =
    RecipeDraftDtoFull(
      id = input.id,
      version = input.version,
      createdOn = input.createdOn,
      lastModifiedOn = input.lastModifiedOn,
      ownedBy = AccountPreviewDtoMapper.mapNotNullBasic(input.ownedBy),
      label = input.label,
      description = input.description,
      prepTime = input.prepTime,
      cookTime = input.cookTime,
      restTime = input.restTime,
      serving = mapServing(input.serving),
      ingredientGroups = input.ingredientGroups.map(::mapIngredientGroup),
      instructionGroups = input.instructionGroups.map(::mapInstructionGroup),
      categories =
          input.categories.map { CategoryDtoMapper.mapNotNullL10n(input = it, language = language) },
      tags = input.tags,
      course = input.course,
      diet = input.diet,
      url = input.url,
      files = input.files.map { RecipeDraftFileEntityRecipeDraftFileDtoMapper.mapNotNullBasic(it) },
      originId = input.originId,
    )

  private fun mapServing(input: RecipeDraftServingEntity) =
    RecipeDraftServingDto(id = input.id, amount = input.amount, label = input.label)

  private fun mapIngredientGroup(input: RecipeDraftIngredientGroupEntity) =
    RecipeDraftIngredientGroupDto(
      id = input.id,
      index = input.index,
      ingredients = input.ingredients.map(::mapIngredientGroupItem),
      label = input.label,
    )

  private fun mapIngredientGroupItem(input: RecipeDraftIngredientGroupItemEntity) =
    RecipeDraftIngredientGroupItemDto(
      id = input.id,
      label = input.label,
      index = input.index,
      amount = input.amount,
      unit = UnitLocalizedDtoMapper.mapBasic(input.unit),
        nutrition = input.nutrition.let(::mapNutrition),
    )

  private fun mapNutrition(input: RecipeDraftIngredientGroupItemNutritionEntity) =
    RecipeDraftIngredientGroupItemNutritionDto(
      id = input.id,
      openFoodFactsId = input.openFoodFactsId,
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

  private fun mapInstructionGroup(input: RecipeDraftInstructionGroupEntity) =
    RecipeDraftInstructionGroupDto(
      id = input.id,
      index = input.index,
      instructions = input.instructions.map(::mapInstructionGroupItem),
      label = input.label,
    )

  private fun mapInstructionGroupItem(input: RecipeDraftInstructionGroupItemEntity) =
    RecipeDraftInstructionGroupItemDto(id = input.id, index = input.index, label = input.label)
}
