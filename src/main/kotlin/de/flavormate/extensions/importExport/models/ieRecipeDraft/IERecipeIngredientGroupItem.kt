/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipeDraft

data class IERecipeIngredientGroupItem(
  val index: Int,
  val label: String?,
  val nutrition: IERecipeIngredientGroupItemNutrition?,
)
