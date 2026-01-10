/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.highlight.daos.models

import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "v3__highlight")
class HighlightEntity : CoreEntity() {
  var date: LocalDate = LocalDate.now()

  @Enumerated(EnumType.STRING) var diet: Diet = Diet.Meat

  @ManyToOne
  @JoinColumn(name = "recipe_id", referencedColumnName = "id")
  lateinit var recipe: RecipeEntity

  companion object {
    fun fromRecipe(date: LocalDate, diet: Diet, recipe: RecipeEntity): HighlightEntity =
      HighlightEntity().apply {
        this.date = date
        this.diet = diet
        this.recipe = recipe
      }
  }
}
