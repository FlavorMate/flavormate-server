/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipe

import de.flavormate.extensions.importExport.models.common.IENutrition

data class IERecipeIngredientGroupItem(
  val index: Int,
  val label: String,
  val nutrition: IENutrition?,
)
