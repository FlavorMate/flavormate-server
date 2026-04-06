/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipeDraft

data class IERecipeDraftInstructionGroup(
  val label: String?,
  val index: Int,
  val instructions: List<IERecipeDraftInstructionGroupItem>,
) {}
