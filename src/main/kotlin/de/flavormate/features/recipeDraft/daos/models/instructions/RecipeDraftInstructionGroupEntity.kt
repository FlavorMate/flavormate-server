/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.models.instructions

import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__recipe_draft__instruction_group")
class RecipeDraftInstructionGroupEntity : CoreEntity() {
  var label: String? = null

  var index: Int = 0

  @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL], orphanRemoval = true)
  var instructions: MutableList<RecipeDraftInstructionGroupItemEntity> = mutableListOf()

  @ManyToOne
  @JoinColumn(name = "recipe_draft_id", referencedColumnName = "id")
  lateinit var recipe: RecipeDraftEntity

  fun generateIndices() {
    instructions.forEachIndexed { index, instruction -> instruction.index = index }
  }

  companion object {
    fun create(
      label: String?,
      index: Int,
      instructions: List<RecipeDraftInstructionGroupItemEntity>,
      recipe: RecipeDraftEntity,
    ): RecipeDraftInstructionGroupEntity {
      return RecipeDraftInstructionGroupEntity().apply {
        this.label = label
        this.index = index
        this.instructions = instructions.toMutableList()
        this.recipe = recipe
      }
    }
  }
}
