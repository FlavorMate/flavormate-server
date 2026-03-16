/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.mappers

import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipe.dtos.mappers.RecipeFileDtoPreviewMapper
import de.flavormate.shared.interfaces.BasicMapper
import java.time.ZoneOffset
import org.schema.models.LDJsonRecipe
import org.schema.models.types.LDJsonPerson
import org.schema.models.types.LDJsonRestrictedDiet
import org.schema.models.types.step.LDJsonHowToSection
import org.schema.models.types.step.LDJsonHowToStep

object LDRecipeRecipeEntityMapper : BasicMapper<RecipeEntity, LDJsonRecipe>() {
  override fun mapNotNullBasic(input: RecipeEntity): LDJsonRecipe {
    return LDJsonRecipe().apply {
      this.cookTime = input.cookTime
      this.nutrition = LDJsonNutritionInformationMapper.mapBasic(input)
      this.recipeCategory = input.categories.map { it.label }
      this.recipeIngredient =
        input.ingredientGroups.flatMap { it.ingredients }.map { it.toJsonString() }
      this.recipeInstructions =
        input.instructionGroups.map {
          LDJsonHowToSection(
            it.label,
            it.index,
            it.instructions.map { LDJsonHowToStep(it.label, it.index) },
          )
        }
      this.recipeYield = input.serving.toString()
      this.suitableForDiet = LDJsonRestrictedDiet.entries.firstOrNull { it.name == input.diet.name }

      this.prepTime = input.prepTime
      this.totalTime = input.totalTime

      this.author = LDJsonPerson(input.ownedBy.displayName)
      this.dateCreated = input.createdOn.toInstant(ZoneOffset.UTC)
      this.dateModified = input.lastModifiedOn.toInstant(ZoneOffset.UTC)
      this.datePublished = input.createdOn.toInstant(ZoneOffset.UTC)
      this.keywords = input.tags.map { it.label }

      this.description = input.description
      this.images = input.files.map { RecipeFileDtoPreviewMapper.mapNotNullBasic(it).path }
      this.name = input.label
      this.url = input.url
    }
  }

  fun mapNotNullWithToken(input: RecipeEntity, path: String, server: String): LDJsonRecipe =
    mapNotNullBasic(input).apply {
      this.images =
        input.files.map {
          RecipeFileDtoPreviewMapper.mapNotNullWithToken(it, server = server, path = path).path
        }
    }
}
