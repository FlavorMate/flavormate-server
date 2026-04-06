/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ld_json.services.exporter

import de.flavormate.extensions.importExport.models.common.IENutrition
import de.flavormate.extensions.importExport.models.ieRecipe.IERecipe
import de.flavormate.extensions.importExport.models.ieRecipe.IERecipeInstructionGroup
import de.flavormate.extensions.importExport.models.ieRecipe.IERecipeInstructionGroupItem
import de.flavormate.extensions.importExport.plugins.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.LDJsonNutritionInformation
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.LDJsonPerson
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.LDJsonRestrictedDiet
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.step.LDJsonHowToSection
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.step.LDJsonHowToStep
import de.flavormate.shared.enums.Language
import de.flavormate.utils.beautify

object IEPluginLDJsonExporter {
  fun export(input: IERecipe, language: Language): LDJsonRecipe {
    return LDJsonRecipe().apply {
      this.cookTime = input.cookTime
      this.nutrition = input.calculateNutrition()?.let { mapNutrition(it) }
      this.recipeCategory = input.categories
      this.recipeIngredient = input.ingredientGroups.flatMap { it.ingredients }.map { it.label }
      this.recipeInstructions = input.instructionGroups.map { mapInstructionGroup(it) }
      this.recipeYield = input.serving.toString()
      this.suitableForDiet = LDJsonRestrictedDiet.fromDiet(input.diet)

      this.prepTime = input.prepTime
      this.totalTime = input.prepTime + input.cookTime + input.restTime

      this.author = LDJsonPerson(name = input.author)
      this.dateCreated = input.createdOn
      this.dateModified = input.lastModifiedOn
      this.datePublished = input.createdOn
      this.inLanguage = language.value
      this.keywords = input.tags

      this.description = input.description
      this.name = input.label
      this.url = input.url
    }
  }

  private fun mapInstructionGroup(input: IERecipeInstructionGroup) =
    LDJsonHowToSection(
      name = input.label,
      position = input.index,
      itemListElement = input.instructions.map { mapInstructionGroupItem(it) },
    )

  private fun mapInstructionGroupItem(input: IERecipeInstructionGroupItem) =
    LDJsonHowToStep(text = input.label, position = input.index)

  private fun mapNutrition(input: IENutrition): LDJsonNutritionInformation {
    return LDJsonNutritionInformation(
      calories = input.energyKcal?.beautify,
      carbohydrateContent = input.carbohydrates?.beautify,
      cholesterolContent = null,
      fatContent = input.fat?.beautify,
      fiberContent = input.fiber?.beautify,
      proteinContent = input.proteins?.beautify,
      saturatedFatContent = input.saturatedFat?.beautify,
      servingSize = null,
      sodiumContent = input.sodium?.beautify,
      sugarContent = input.sugars?.beautify,
      transFatContent = null,
      unsaturatedFatContent = null,
    )
  }
}
