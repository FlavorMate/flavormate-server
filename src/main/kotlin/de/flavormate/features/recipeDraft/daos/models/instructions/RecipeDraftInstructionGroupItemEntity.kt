/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.models.instructions

import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "v3__recipe_draft__instruction_group__item")
class RecipeDraftInstructionGroupItemEntity : CoreEntity() {
  var label: String? = null

  var index: Int = 0

  @ManyToOne
  @JoinColumn(name = "group_id", referencedColumnName = "id")
  lateinit var group: RecipeDraftInstructionGroupEntity

  companion object {
    fun create(
      label: String?,
      index: Int,
      group: RecipeDraftInstructionGroupEntity,
      id: String? = null,
    ): RecipeDraftInstructionGroupItemEntity =
      RecipeDraftInstructionGroupItemEntity().apply {
        this.label = label
        this.index = index
        this.group = group
        id?.let { this.id = it }
      }
  }
}
