/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.services

import de.flavormate.extensions.importExport.models.common.IENutrition
import de.flavormate.extensions.importExport.models.ieRecipe.*
import de.flavormate.features.category.daos.models.CategoryEntity
import de.flavormate.features.category.repositories.CategoryRepository
import de.flavormate.features.recipe.daos.models.NutritionEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipe.daos.models.ServingEntity
import de.flavormate.features.recipe.daos.models.ingredient.IngredientEntity
import de.flavormate.features.recipe.daos.models.ingredient.IngredientGroupEntity
import de.flavormate.features.recipe.daos.models.instruction.InstructionEntity
import de.flavormate.features.recipe.daos.models.instruction.InstructionGroupEntity
import de.flavormate.features.recipeDraft.repositories.RecipeDraftFileRepository
import de.flavormate.features.recipeDraft.repositories.RecipeDraftRepository
import de.flavormate.features.unit.repositories.UnitLocalizedRepository
import de.flavormate.shared.enums.Language
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.FileService
import de.flavormate.utils.beautify
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import java.time.ZoneOffset
import org.apache.commons.lang3.StringUtils

@RequestScoped
class IERecipeConvertService(
  private val authorizationDetails: AuthorizationDetails,
  private val categoryRepository: CategoryRepository,
  private val unitLocalizedRepository: UnitLocalizedRepository,
  private val recipeDraftRepository: RecipeDraftRepository,
  private val fileRecipeDraftRepository: RecipeDraftFileRepository,
  private val fileService: FileService,
) {

  @Transactional
  fun map(input: RecipeEntity, language: Language): IERecipe {

    val r =
      IERecipe(
        language = language,
        cookTime = input.cookTime,
        course = input.course,
        description = input.description,
        diet = input.diet,
        label = input.label,
        prepTime = input.prepTime,
        restTime = input.restTime,
        serving = mapServing(input.serving),
        instructionGroups = input.instructionGroups.map { mapInstructionGroup(it) },
        ingredientGroups = input.ingredientGroups.map { mapIngredientGroup(it) },
        categories = input.categories.map { mapCategory(it, language) },
        tags = input.tags.map { it.label },
        files = listOf(), // input.files.map { mapFile(it, language) },
        url = input.url,
        author = input.ownedBy.displayName,
        createdOn = input.createdOn.toInstant(ZoneOffset.UTC),
        lastModifiedOn = input.lastModifiedOn.toInstant(ZoneOffset.UTC),
      )
    return r
  }

  private fun mapCategory(input: CategoryEntity, language: Language): String {
    input.translate(language.value)
    return input.label
  }

  private fun mapServing(input: ServingEntity): IERecipeServing {
    return IERecipeServing(amount = input.amount, label = input.label)
  }

  private fun mapInstructionGroup(input: InstructionGroupEntity): IERecipeInstructionGroup {
    return IERecipeInstructionGroup(
      label = input.label,
      index = input.index,
      instructions = input.instructions.map { mapInstructionGroupItem(it) },
    )
  }

  private fun mapInstructionGroupItem(input: InstructionEntity): IERecipeInstructionGroupItem {
    return IERecipeInstructionGroupItem(label = input.label, index = input.index)
  }

  private fun mapIngredientGroup(input: IngredientGroupEntity): IERecipeIngredientGroup {

    return IERecipeIngredientGroup(
      label = input.label,
      index = input.index,
      ingredients = input.ingredients.map { mapIngredientGroupItem(it) },
    )
  }

  private fun mapIngredientGroupItem(input: IngredientEntity): IERecipeIngredientGroupItem {
    val amountLabel: String? = input.amount?.beautify

    val unitLabel = input.unit?.getLongLabel(input.amount)

    val label =
      listOfNotNull(amountLabel, unitLabel, input.label)
        .filter(StringUtils::isNotBlank)
        .joinToString(" ")

    return IERecipeIngredientGroupItem(
      label = label,
      index = input.index,
      nutrition = input.nutrition?.let { mapNutrition(it) }?.takeUnless { it.isEmpty },
    )
  }

  private fun mapNutrition(input: NutritionEntity): IENutrition {
    return IENutrition(
      openFoodFactsId = input.openFoodFactsId?.id,
      carbohydrates = input.carbohydrates,
      energyKcal = input.energyKcal,
      fat = input.fat,
      saturatedFat = input.saturatedFat,
      sugars = input.sugars,
      fiber = input.fiber,
      proteins = input.proteins,
      salt = input.salt,
      sodium = input.sodium,
    )
  }
}
