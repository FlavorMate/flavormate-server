/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.flavormate.models

data class IEFlavorMateRecipeInstructionGroup(
  val label: String?,
  val index: Int,
  val instructions: List<IEFlavorMateRecipeInstructionGroupItem>,
)
