/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.daos.models

import de.flavormate.extensions.openFoodFacts.dao.models.OFFProductEntity
import de.flavormate.features.recipe.daos.models.ingredient.IngredientEntity
import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__recipe__ingredient_group__item__nutrition")
class NutritionEntity : CoreEntity() {
  @ManyToOne
  @JoinColumn(name = "open_food_facts_id", referencedColumnName = "id")
  var openFoodFactsId: OFFProductEntity? = null

  var carbohydrates: Double? = null

  @Column(name = "energy_kcal") var energyKcal: Double? = null

  var fat: Double? = null

  @Column(name = "saturated_fat") var saturatedFat: Double? = null

  var sugars: Double? = null

  var fiber: Double? = null

  var proteins: Double? = null

  var salt: Double? = null

  var sodium: Double? = null

  @OneToOne(mappedBy = "nutrition") var ingredient: IngredientEntity? = null
}
