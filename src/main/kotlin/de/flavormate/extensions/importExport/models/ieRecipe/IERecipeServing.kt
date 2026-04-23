/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipe

data class IERecipeServing(val amount: Double, val label: String) {
  override fun toString(): String = "$amount $label"
}
