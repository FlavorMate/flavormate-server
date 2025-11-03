/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.mappers

import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonNutritionInformation
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.shared.interfaces.BasicMapper

object LDJsonNutritionInformationMapper : BasicMapper<RecipeEntity, LDJsonNutritionInformation>() {
  override fun mapNotNullBasic(input: RecipeEntity): LDJsonNutritionInformation {
    return LDJsonNutritionInformation("", "", "", "", "", "", "", "", "", "", "", "")
  }
}
