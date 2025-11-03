/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.mapper

import de.flavormate.features.recipe.daos.models.instruction.InstructionGroupEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupEntity
import de.flavormate.shared.interfaces.BasicMapper

object InstructionGroupDraftEntityInstructionGroupEntityMapper :
  BasicMapper<InstructionGroupEntity, RecipeDraftInstructionGroupEntity>() {
  override fun mapNotNullBasic(input: InstructionGroupEntity): RecipeDraftInstructionGroupEntity {
    return RecipeDraftInstructionGroupEntity().apply {
      this.label = input.label
      this.index = input.index
      this.instructions =
        input.instructions.mapTo(mutableListOf()) { instruction ->
          InstructionDraftEntityInstructionEntityMapper.mapNotNullBasic(instruction).also {
            it.group = this
          }
        }
    }
  }
}
