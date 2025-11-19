/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.models.ingredients

import de.flavormate.features.recipeDraft.daos.models.RecipeDraftIngredientGroupItemNutritionEntity
import de.flavormate.features.unit.daos.models.UnitLocalizedEntity
import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__recipe_draft__ingredient_group__item")
class RecipeDraftIngredientGroupItemEntity : CoreEntity() {

  var index: Int = 0

  var amount: Double? = null

  var label: String? = null

  @ManyToOne
  @JoinColumn(name = "unit", referencedColumnName = "id")
  var unit: UnitLocalizedEntity? = null

  @ManyToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "nutrition_id", referencedColumnName = "id")
  lateinit var nutrition: RecipeDraftIngredientGroupItemNutritionEntity

  @ManyToOne
  @JoinColumn(name = "group_id", referencedColumnName = "id")
  lateinit var group: RecipeDraftIngredientGroupEntity

  companion object {
    fun create(
      label: String?,
      index: Int,
      group: RecipeDraftIngredientGroupEntity,
      id: String? = null,
    ): RecipeDraftIngredientGroupItemEntity =
      RecipeDraftIngredientGroupItemEntity().apply {
        this.label = label
        this.index = index
        this.nutrition = RecipeDraftIngredientGroupItemNutritionEntity()
        this.group = group
        id?.let { this.id = it }
      }
  }
}
