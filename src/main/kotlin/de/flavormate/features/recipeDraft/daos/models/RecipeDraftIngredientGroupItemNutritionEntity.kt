/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.models

import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "v3__recipe_draft__ingredient_group__item__nutrition")
class RecipeDraftIngredientGroupItemNutritionEntity : CoreEntity() {
  @Column(name = "open_food_facts_id") var openFoodFactsId: String? = null

  var carbohydrates: Double? = null

  @Column(name = "energy_kcal") var energyKcal: Double? = null

  var fat: Double? = null

  @Column(name = "saturated_fat") var saturatedFat: Double? = null

  var sugars: Double? = null

  var fiber: Double? = null

  var proteins: Double? = null

  var salt: Double? = null

  var sodium: Double? = null

  val isEmpty
    get() = openFoodFactsId.isNullOrBlank() && !hasAnyNutritionalValue()

  private fun hasAnyNutritionalValue() =
    listOf(carbohydrates, energyKcal, fat, fiber, proteins, salt, saturatedFat, sodium, sugars)
      .any { it != null && it > 0 }
}
