/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.mappers

import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.shared.interfaces.BasicMapper
import org.schema.models.types.LDJsonNutritionInformation

object LDJsonNutritionInformationMapper : BasicMapper<RecipeEntity, LDJsonNutritionInformation>() {
  override fun mapNotNullBasic(input: RecipeEntity): LDJsonNutritionInformation {
    return LDJsonNutritionInformation("", "", "", "", "", "", "", "", "", "", "", "")
  }
}
