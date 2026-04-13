/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipeDraft

import de.flavormate.extensions.importExport.models.common.IENutrition

data class IERecipeDraftIngredientGroupItem(
  val index: Int,
  val label: String?,
  val nutrition: IENutrition?,
)
