/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.time.Duration
import org.schema.models.types.LDJsonNutritionInformation
import org.schema.models.types.LDJsonRestrictedDiet
import org.schema.models.types.step.LDJsonStep

class LDJsonRecipe : LDJsonHowTo() {
  @JsonProperty("@type") override val type = "Recipe"

  var cookTime: Duration = Duration.ZERO

  var cookingMethod: String? = null

  var nutrition: LDJsonNutritionInformation? = null

  @JsonDeserialize(using = org.schema.serializers.LDJsonDefinedTermDeserializer::class)
  var recipeCategory: List<String> = listOf()

  @JsonDeserialize(using = org.schema.serializers.LDJsonDefinedTermDeserializer::class)
  var recipeCuisine: List<String> = listOf()

  @JsonDeserialize(using = org.schema.serializers.LDJsonStringDeserializer::class)
  var recipeIngredient: List<String> = listOf()

  @JsonDeserialize(using = org.schema.serializers.LDJsonStepDeserializer::class)
  var recipeInstructions: List<LDJsonStep> = listOf()

  @JsonDeserialize(using = org.schema.serializers.LDJsonRecipeYieldDeserializer::class)
  var recipeYield: String? = null

  @JsonDeserialize(using = org.schema.serializers.LDJsonRestrictedDietDeserializer::class)
  var suitableForDiet: LDJsonRestrictedDiet? = null
}
