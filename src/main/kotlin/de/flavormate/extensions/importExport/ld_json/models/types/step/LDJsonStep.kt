/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.models.types.step

interface LDJsonStep {
  fun toStepList(): List<String>

  fun flatten(): List<LDJsonStep>
}
