/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.mapper

import de.flavormate.features.recipe.daos.models.instruction.InstructionEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupItemEntity
import de.flavormate.shared.interfaces.BasicMapper

object InstructionDraftEntityInstructionEntityMapper :
  BasicMapper<InstructionEntity, RecipeDraftInstructionGroupItemEntity>() {
  override fun mapNotNullBasic(input: InstructionEntity): RecipeDraftInstructionGroupItemEntity {
    return RecipeDraftInstructionGroupItemEntity().apply {
      this.index = input.index
      this.label = input.label
    }
  }
}
