/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipe

data class IERecipeInstructionGroup(
  val label: String?,
  val index: Int,
  val instructions: List<IERecipeInstructionGroupItem>,
) {}
