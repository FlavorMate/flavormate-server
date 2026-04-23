/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.flavormate.models

import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.enums.Language
import java.time.Duration

data class IEFlavorMateRecipe(
  val language: Language,
  val cookTime: Duration,
  val course: Course,
  var description: String?,
  val diet: Diet,
  var label: String,
  val prepTime: Duration,
  val restTime: Duration,
  val serving: IEFlavorMateRecipeServing,
  val instructionGroups: List<IEFlavorMateRecipeInstructionGroup>,
  val ingredientGroups: List<IEFlavorMateRecipeIngredientGroup>,
  val categories: List<String>,
  val tags: List<String>,
  val url: String?,
)
