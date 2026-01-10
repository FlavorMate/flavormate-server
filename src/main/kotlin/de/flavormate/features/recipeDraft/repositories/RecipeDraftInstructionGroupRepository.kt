/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.repositories

import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecipeDraftInstructionGroupRepository :
  PanacheRepositoryBase<RecipeDraftInstructionGroupEntity, String> {
  fun existsById(id: String): Boolean {
    val params = mapOf("id" to id)
    return count(query = "id = :id", params = params) > 0
  }
}
