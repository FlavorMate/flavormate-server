/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ldJson.models.types.step

import de.flavormate.extensions.importExport.plugins.ldJson.models.types.LDJsonSchema

/**
 * Handles both [HowToStep](https://schema.org/HowToStep) and [Text](https://schema.org/Text). The
 * difference is that "Text" has no position value.
 */
data class LDJsonHowToStep(val text: String, val position: Int?) :
  LDJsonSchema("HowToStep"), LDJsonStep {
  override fun toStepList(): List<String> = listOf(text)

  override fun flatten(): List<LDJsonStep> = listOf(this)
}
