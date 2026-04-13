/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipe

import de.flavormate.extensions.importExport.models.common.IENutrition
import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.enums.Language
import java.io.File
import java.time.Duration
import java.time.Instant

data class IERecipe(
  val language: Language,
  val id: String,
  val cookTime: Duration,
  val course: Course,
  var description: String?,
  val diet: Diet,
  var label: String,
  val prepTime: Duration,
  val restTime: Duration,
  val serving: IERecipeServing,
  val instructionGroups: List<IERecipeInstructionGroup>,
  val ingredientGroups: List<IERecipeIngredientGroup>,
  val categories: List<String>,
  val tags: List<String>,
  val files: List<File>,
  val url: String?,
  val author: String,
  val createdOn: Instant,
  val lastModifiedOn: Instant,
) {
  fun calculateNutrition(): IENutrition? {
    return ingredientGroups
      .flatMap { it.ingredients }
      .map { it.nutrition }
      .fold(IENutrition.empty) { a, b -> a.plus(b) }
      .takeUnless { it.isEmpty }
  }
}
