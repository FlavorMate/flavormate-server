/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipeDraft

import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.enums.Language
import java.io.File
import java.time.Duration

data class IERecipeDraft(
  val language: Language,
  val cookTime: Duration?,
  val course: Course?,
  var description: String?,
  val diet: Diet?,
  var label: String?,
  val prepTime: Duration?,
  val restTime: Duration?,
  val serving: IERecipeDraftServing,
  val instructionGroups: List<IERecipeDraftInstructionGroup>,
  val ingredientGroups: List<IERecipeDraftIngredientGroup>,
  val categories: List<String>,
  val tags: List<String>,
  val files: List<File>,
  val url: String?,
)
