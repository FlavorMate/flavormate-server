/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.daos.mappers.instruction

import de.flavormate.features.recipe.daos.models.instruction.InstructionGroupEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupEntity
import de.flavormate.shared.interfaces.BasicMapper

object InstructionGroupEntityInstructionGroupDraftEntityMapper :
  BasicMapper<RecipeDraftInstructionGroupEntity, InstructionGroupEntity>() {
  override fun mapNotNullBasic(input: RecipeDraftInstructionGroupEntity): InstructionGroupEntity {
    return InstructionGroupEntity().apply {
      this.label = input.label
      this.index = input.index
      this.instructions =
        input.instructions.mapTo(mutableListOf()) { instruction ->
          InstructionEntityInstructionDraftEntityMapper.mapNotNullBasic(instruction).also {
            it.group = this
          }
        }
    }
  }
}
