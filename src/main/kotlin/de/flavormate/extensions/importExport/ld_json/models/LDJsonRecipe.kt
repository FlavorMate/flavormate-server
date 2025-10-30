/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonNutritionInformation
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonRestrictedDiet
import de.flavormate.extensions.importExport.ld_json.models.types.step.LDJsonStep
import de.flavormate.extensions.importExport.ld_json.serializers.*
import java.time.Duration

// @JsonIgnoreProperties(ignoreUnknown = true)
class LDJsonRecipe : LDJsonHowTo() {
  @JsonProperty("@type") override val type = "Recipe"

  var cookTime: Duration = Duration.ZERO

  var cookingMethod: String? = null

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
