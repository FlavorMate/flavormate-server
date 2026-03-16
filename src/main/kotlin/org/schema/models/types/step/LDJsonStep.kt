/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.models.types.step

interface LDJsonStep {
  fun toStepList(): List<String>

  fun flatten(): List<LDJsonStep>
}
