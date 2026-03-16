/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.models.types.step

import org.schema.models.types.LDJsonSchema

/**
 * Handles both [HowToStep](https://schema.org/HowToStep) and [Text](https://schema.org/Text). The
 * difference is that "Text" has no position value.
 */
data class LDJsonHowToStep(val text: String, val position: Int?) :
  LDJsonStep, LDJsonSchema("HowToStep") {

  override fun toStepList(): List<String> {
    return listOf(text)
  }

  override fun flatten(): List<LDJsonStep> {
    return listOf(this)
  }
}
