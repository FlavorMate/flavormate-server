/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.daos.models.instruction

import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__recipe__instruction_group")
class InstructionGroupEntity : CoreEntity() {
  var label: String? = null

  var index: Int = -1

  @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL], orphanRemoval = true)
  var instructions: MutableList<InstructionEntity> = mutableListOf()

  @ManyToOne
  @JoinColumn(name = "recipe_id", referencedColumnName = "id")
  lateinit var recipe: RecipeEntity
}
