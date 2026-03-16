/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.time.Duration
import org.schema.models.types.LDJsonNutritionInformation
import org.schema.models.types.LDJsonRestrictedDiet
import org.schema.models.types.step.LDJsonStep
import org.schema.serializers.*

class LDJsonRecipe : LDJsonHowTo() {
  @JsonProperty("@type") override val type = "Recipe"

  var cookTime: Duration = Duration.ZERO

  var cookingMethod: String? = null

  @JsonDeserialize(using = LDJsonNutritionDeserializer::class)
  var nutrition: LDJsonNutritionInformation? = null

  @JsonDeserialize(using = LDJsonDefinedTermDeserializer::class)
  var recipeCategory: List<String> = listOf()

  @JsonDeserialize(using = LDJsonDefinedTermDeserializer::class)
  var recipeCuisine: List<String> = listOf()

  @JsonDeserialize(using = LDJsonStringDeserializer::class)
  var recipeIngredient: List<String> = listOf()

  @JsonDeserialize(using = LDJsonStepDeserializer::class)
  var recipeInstructions: List<LDJsonStep> = listOf()

  @JsonDeserialize(using = LDJsonRecipeYieldDeserializer::class) var recipeYield: String? = null

  @JsonDeserialize(using = LDJsonRestrictedDietDeserializer::class)
  var suitableForDiet: LDJsonRestrictedDiet? = null
}
