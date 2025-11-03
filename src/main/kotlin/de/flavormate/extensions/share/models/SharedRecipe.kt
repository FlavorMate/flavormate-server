/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.share.models

data class SharedRecipe(
  val id: String,
  val label: String,
  val cover: String?,
  val description: String?,
  val prepTime: String?,
  val cookTime: String?,
  val restTime: String?,
  val serving: String,
  val ingredientGroups: List<SharedIngredientGroup>,
  val instructionGroups: List<SharedInstructionGroup>,
  val diet: String,
  val course: String,
  val categories: List<String>,
  val tags: List<String>,
  val createdOn: String,
  val version: Long,
  val url: String?,
)
