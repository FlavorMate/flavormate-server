/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ld_json.models.types

import de.flavormate.shared.enums.Diet

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
      entries.firstOrNull { it.name.equals(string, ignoreCase = true) }

    fun fromDiet(diet: Diet) =
      when (diet) {
        Diet.Vegan -> VeganDiet
        Diet.Vegetarian -> VegetarianDiet
        else -> null
      }
  }
}
