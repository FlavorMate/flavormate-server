/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ldJson.models.types.step

interface LDJsonStep {
  fun toStepList(): List<String>

  fun flatten(): List<LDJsonStep>
}
