/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.flavormate.services.exporter

import de.flavormate.extensions.importExport.models.ieRecipe.*
import de.flavormate.extensions.importExport.plugins.flavormate.models.*

class IEFlavorMateExporter {
  fun export(input: IERecipe): IEFlavorMateRecipe =
    IEFlavorMateRecipe(
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
      url = input.url,
    )

  private fun mapServing(input: IERecipeServing) =
    IEFlavorMateRecipeServing(amount = input.amount, label = input.label)

  private fun mapInstructionGroup(input: IERecipeInstructionGroup) =
    IEFlavorMateRecipeInstructionGroup(
      index = input.index,
      label = input.label,
      instructions = input.instructions.map { mapInstructionGroupItem(it) },
    )

  private fun mapInstructionGroupItem(input: IERecipeInstructionGroupItem) =
    IEFlavorMateRecipeInstructionGroupItem(index = input.index, label = input.label)

  private fun mapIngredientGroup(input: IERecipeIngredientGroup) =
    IEFlavorMateRecipeIngredientGroup(
      index = input.index,
      label = input.label,
      ingredients = input.ingredients.map { mapIngredientGroupItem(it) },
    )

  private fun mapIngredientGroupItem(input: IERecipeIngredientGroupItem) =
    IEFlavorMateRecipeIngredientGroupItem(
      index = input.index,
      label = input.label,
      nutrition = input.nutrition,
    )
}
