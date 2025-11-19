/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.repositories

import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupItemEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecipeDraftInstructionGroupItemRepository :
  PanacheRepositoryBase<RecipeDraftInstructionGroupItemEntity, String> {
  fun existsById(id: String): Boolean {
    val params = mapOf("id" to id)
    return count(query = "id = :id", params = params) > 0
  }
}
