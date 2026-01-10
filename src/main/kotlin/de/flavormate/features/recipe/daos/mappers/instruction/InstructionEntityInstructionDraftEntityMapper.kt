/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.daos.mappers.instruction

import de.flavormate.features.recipe.daos.models.instruction.InstructionEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupItemEntity
import de.flavormate.shared.interfaces.BasicMapper

object InstructionEntityInstructionDraftEntityMapper :
  BasicMapper<RecipeDraftInstructionGroupItemEntity, InstructionEntity>() {
  override fun mapNotNullBasic(input: RecipeDraftInstructionGroupItemEntity): InstructionEntity {
    return InstructionEntity().apply {
      this.label = input.label!!
      this.index = input.index
    }
  }
}
