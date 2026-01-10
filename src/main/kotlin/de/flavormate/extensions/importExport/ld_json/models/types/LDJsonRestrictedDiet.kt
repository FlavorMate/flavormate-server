/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.ld_json.models.types

enum class LDJsonRestrictedDiet {
  DiabeticDiet,
  GlutenFreeDiet,
  HalalDiet,
  HinduDiet,
  KosherDiet,
  LowCalorieDiet,
  LowFatDiet,
  LowLactoseDiet,
  LowSaltDiet,
  VeganDiet,
  VegetarianDiet;

  companion object {
    fun fromString(string: String): LDJsonRestrictedDiet? =
      entries.firstOrNull { it.name.lowercase() == string.lowercase() }
  }
}
