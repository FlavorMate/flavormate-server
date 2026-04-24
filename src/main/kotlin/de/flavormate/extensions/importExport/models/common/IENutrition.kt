/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.common

import com.fasterxml.jackson.annotation.JsonIgnore
import de.flavormate.utils.plus

data class IENutrition(
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
  @get:JsonIgnore
  val isEmpty
    get() = openFoodFactsId.isNullOrBlank() && !hasAnyNutritionalValue

  @get:JsonIgnore
  val hasAnyNutritionalValue
    get() =
      listOf(carbohydrates, energyKcal, fat, fiber, proteins, salt, saturatedFat, sodium, sugars)
        .any { it != null && it > 0 }

  fun plus(other: IENutrition?): IENutrition {
    if (other == null) return this

    return IENutrition(
      openFoodFactsId = null,
      carbohydrates = this.carbohydrates + other.carbohydrates,
      energyKcal = this.energyKcal + other.energyKcal,
      fat = this.fat + other.fat,
      saturatedFat = this.saturatedFat + other.saturatedFat,
      sugars = this.sugars + other.sugars,
      fiber = this.fiber + other.fiber,
      proteins = this.proteins + other.proteins,
      salt = this.salt + other.salt,
      sodium = this.sodium + other.sodium,
    )
  }

  companion object {
    val empty
      get() = IENutrition(null, null, null, null, null, null, null, null, null, null)
  }
}
