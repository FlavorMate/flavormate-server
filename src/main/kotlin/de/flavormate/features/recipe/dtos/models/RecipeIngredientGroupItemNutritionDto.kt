/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.dtos.models

data class RecipeIngredientGroupItemNutritionDto(
  val id: String,
  val openFoodFactsId: String? = null,
  val carbohydrates: Double? = null,
  val energyKcal: Double? = null,
  val fat: Double? = null,
  val fiber: Double? = null,
  val proteins: Double? = null,
  val salt: Double? = null,
  val saturatedFat: Double? = null,
  val sodium: Double? = null,
  val sugars: Double? = null,
)
