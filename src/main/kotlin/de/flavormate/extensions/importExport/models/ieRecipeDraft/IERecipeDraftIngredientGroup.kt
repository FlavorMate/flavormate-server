/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipeDraft

data class IERecipeDraftIngredientGroup(
  var label: String?,
  var index: Int,
  var ingredients: List<IERecipeDraftIngredientGroupItem>,
) {}
