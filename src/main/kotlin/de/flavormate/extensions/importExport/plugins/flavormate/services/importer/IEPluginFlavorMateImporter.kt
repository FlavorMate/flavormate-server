/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.flavormate.services.importer

import de.flavormate.extensions.importExport.models.ieRecipeDraft.*
import de.flavormate.extensions.importExport.plugins.flavormate.models.*
import java.io.File

class IEPluginFlavorMateImporter {

  fun import(input: IEFlavorMateRecipe, files: List<File>?): IERecipeDraft {
    return IERecipeDraft(
      language = input.language,
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
      categories = input.categories,
      tags = input.tags,
      files = files ?: emptyList(),
      url = input.url,
    )
  }

  private fun mapServing(input: IEFlavorMateRecipeServing) =
    IERecipeDraftServing(amount = input.amount, label = input.label)

  private fun mapInstructionGroup(input: IEFlavorMateRecipeInstructionGroup) =
    IERecipeDraftInstructionGroup(
      index = input.index,
      label = input.label,
      instructions = input.instructions.map { mapInstructionGroupItem(it) },
    )

  private fun mapInstructionGroupItem(input: IEFlavorMateRecipeInstructionGroupItem) =
    IERecipeDraftInstructionGroupItem(index = input.index, label = input.label)

  private fun mapIngredientGroup(input: IEFlavorMateRecipeIngredientGroup) =
    IERecipeDraftIngredientGroup(
      index = input.index,
      label = input.label,
      ingredients = input.ingredients.map { mapIngredientGroupItem(it) },
    )

  private fun mapIngredientGroupItem(input: IEFlavorMateRecipeIngredientGroupItem) =
    IERecipeDraftIngredientGroupItem(
      index = input.index,
      label = input.label,
      nutrition = input.nutrition,
    )
}
