/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipe

data class IERecipeIngredientGroup(
  var label: String?,
  var index: Int,
  var ingredients: List<IERecipeIngredientGroupItem>,
)
