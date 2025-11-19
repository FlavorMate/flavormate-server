/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.models.ingredients

import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__recipe_draft__ingredient_group")
class RecipeDraftIngredientGroupEntity : CoreEntity() {

  var label: String? = null

  var index: Int = 0

  @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL], orphanRemoval = true)
  var ingredients: MutableList<RecipeDraftIngredientGroupItemEntity> = mutableListOf()

  @ManyToOne
  @JoinColumn(name = "recipe_draft_id", referencedColumnName = "id")
  lateinit var recipe: RecipeDraftEntity

  fun generateIndices() {
    ingredients.forEachIndexed { index, ingredient -> ingredient.index = index }
  }

  companion object {
    fun create(
      label: String?,
      index: Int,
      ingredients: List<RecipeDraftIngredientGroupItemEntity>,
      recipe: RecipeDraftEntity,
      id: String? = null,
    ): RecipeDraftIngredientGroupEntity {
      return RecipeDraftIngredientGroupEntity().apply {
        this.label = label
        this.index = index
        this.ingredients = ingredients.toMutableList()
        this.recipe = recipe
        id?.let { this.id = it }
      }
    }
  }
}
