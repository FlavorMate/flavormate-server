/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.flavormate.models

data class IEFlavorMateRecipeIngredientGroup(
  var label: String?,
  var index: Int,
  var ingredients: List<IEFlavorMateRecipeIngredientGroupItem>,
) {}
