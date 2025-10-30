/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.mappers

import de.flavormate.extensions.importExport.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonPerson
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonRestrictedDiet
import de.flavormate.extensions.importExport.ld_json.models.types.step.LDJsonHowToSection
import de.flavormate.extensions.importExport.ld_json.models.types.step.LDJsonHowToStep
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.shared.interfaces.BasicMapper
import java.time.ZoneOffset

object LDRecipeRecipeEntityMapper : BasicMapper<RecipeEntity, LDJsonRecipe>() {
  override fun mapNotNullBasic(input: RecipeEntity): LDJsonRecipe {
    return LDJsonRecipe().apply {
      this.cookTime = input.cookTime
      this.cookingMethod
      this.nutrition = LDJsonNutritionInformationMapper.mapBasic(input)
      this.recipeCategory = input.categories.map { it.label }
      this.recipeIngredient =
        input.ingredientGroups.flatMap { it.ingredients }.map { it.toString() }
      this.recipeInstructions =
        input.instructionGroups.map {
          LDJsonHowToSection(
            it.label,
            it.index,
            it.instructions.map { LDJsonHowToStep(it.label, it.index) },
          )
        }
      this.recipeYield = input.serving.toString()
      this.suitableForDiet =
        LDJsonRestrictedDiet.entries.firstOrNull { it.name == input.diet!!.name }

      this.prepTime = input.prepTime
      this.totalTime = input.totalTime

      this.author = LDJsonPerson(input.ownedBy!!.displayName!!)
      this.dateCreated = input.createdOn.toInstant(ZoneOffset.UTC)
      this.dateModified = input.lastModifiedOn.toInstant(ZoneOffset.UTC)
      this.datePublished = input.createdOn.toInstant(ZoneOffset.UTC)
      this.keywords = input.tags.map { it.label }

      this.description = input.description
      this.images = input.files.map { "http://localhost:8080/v3/files/${it.id}" }
      this.name = input.label
      this.url = input.url
    }
  }
}
