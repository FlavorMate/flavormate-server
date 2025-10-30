/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.dtos.models.update

data class RecipeDraftIngredientGroupItemNutritionUpdateDto(
  val openFoodFactsId: String?,
  val carbohydrates: Double?,
  val energyKcal: Double?,
  val fat: Double?,
  val fiber: Double?,
  val proteins: Double?,
  val salt: Double?,
  val saturatedFat: Double?,
  val sodium: Double?,
  val sugars: Double?,
)
