/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.daos.models.ingredient

import de.flavormate.features.recipe.daos.models.NutritionEntity
import de.flavormate.features.unit.daos.models.UnitLocalizedEntity
import de.flavormate.shared.models.entities.CoreEntity
import de.flavormate.utils.NumberUtils
import jakarta.persistence.*
import org.apache.commons.lang3.StringUtils

@Entity
@Table(name = "v3__recipe__ingredient_group__item")
class IngredientEntity : CoreEntity() {
  var amount: Double? = null

  lateinit var label: String

  var index: Int = -1

  @ManyToOne
  @JoinColumn(name = "group_id", referencedColumnName = "id")
  lateinit var group: IngredientGroupEntity

  @ManyToOne
  @JoinColumn(name = "unit", referencedColumnName = "id")
  var unit: UnitLocalizedEntity? = null

  @ManyToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "nutrition_id", referencedColumnName = "id")
  var nutrition: NutritionEntity? = null

  override fun toString(): String {
    val amountLabel: String? = amount?.let { NumberUtils.beautify(it) }

    val unitLabel = unit?.getLabel(amount)

    return listOfNotNull(amountLabel, unitLabel, label)
      .filter(StringUtils::isNotBlank)
      .joinToString(" ")
  }

  fun requestServing(factor: Double): String {
    val requestedAmount = amount?.times(factor) ?: 1.0

    val amountLabel: String? = amount?.let { NumberUtils.beautify(requestedAmount) }

    val unitLabel: String? = unit?.getLabel(requestedAmount)

    return listOf(amountLabel, unitLabel, label).filter(StringUtils::isNotBlank).joinToString(" ")
  }
}
