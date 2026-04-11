/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.nextcloud_cookbook.services.exporter

import de.flavormate.extensions.importExport.models.common.IENutrition
import de.flavormate.extensions.importExport.models.ieRecipe.IERecipe
import de.flavormate.extensions.importExport.models.ieRecipe.IERecipeInstructionGroupItem
import de.flavormate.extensions.importExport.plugins.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.LDJsonNutritionInformation
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.LDJsonPerson
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.LDJsonRestrictedDiet
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.step.LDJsonHowToStep
import de.flavormate.utils.beautify

class IEPluginLDNextcloudCookbookExporter() {
  fun export(input: IERecipe): LDJsonRecipe {
    return LDJsonRecipe().apply {
      this.cookTime = input.cookTime
      this.nutrition = input.calculateNutrition()?.let { mapNutrition(it) }
      this.recipeCategory = input.categories
      this.recipeIngredient = input.ingredientGroups.flatMap { it.ingredients }.map { it.label }
      this.recipeInstructions =
        input.instructionGroups
          .flatMap { it.instructions }
          .mapIndexed { index, it -> mapInstructionGroupItem(it, index) }
      this.recipeYield = input.serving.toString()
      this.suitableForDiet = LDJsonRestrictedDiet.fromDiet(input.diet)

      this.prepTime = input.prepTime
      this.totalTime = input.prepTime + input.cookTime + input.restTime

      this.author = LDJsonPerson(name = input.author)
      this.dateCreated = input.createdOn
      this.dateModified = input.lastModifiedOn
      this.datePublished = input.createdOn
      this.inLanguage = input.language.value
      this.keywords = input.tags

      this.description = input.description
      this.name = input.label
      this.url = input.url
    }
  }

  private fun mapInstructionGroupItem(input: IERecipeInstructionGroupItem, index: Int) =
    LDJsonHowToStep(text = input.label, position = index)

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
