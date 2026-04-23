/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.flavormate.models

import de.flavormate.extensions.importExport.models.common.IENutrition

data class IEFlavorMateRecipeIngredientGroupItem(
  val index: Int,
  val label: String,
  val nutrition: IENutrition?,
)
