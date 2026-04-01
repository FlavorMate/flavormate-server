/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.ieRecipeDraft

data class IERecipeIngredientGroupItemNutrition(
  val openFoodFactsId: String?,
  val carbohydrates: Double?,
  val energyKcal: Double?,
  val fat: Double?,
  val saturatedFat: Double?,
  val sugars: Double?,
  val fiber: Double?,
  val proteins: Double?,
  val salt: Double?,
  val sodium: Double?,
) {

  val isEmpty
    get() = openFoodFactsId.isNullOrBlank() && !hasAnyNutritionalValue

  val hasAnyNutritionalValue
    get() =
      listOf(carbohydrates, energyKcal, fat, fiber, proteins, salt, saturatedFat, sodium, sugars)
        .any { it != null && it > 0 }
}
