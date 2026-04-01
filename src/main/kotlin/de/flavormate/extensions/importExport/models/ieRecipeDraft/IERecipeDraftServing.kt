/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipeDraft

data class IERecipeDraftServing(val amount: Double?, val label: String?) {
  companion object {
    fun empty() = IERecipeDraftServing(null, null)
  }
}
