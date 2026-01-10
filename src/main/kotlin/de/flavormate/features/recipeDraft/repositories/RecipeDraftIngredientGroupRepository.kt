/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.repositories

import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecipeDraftIngredientGroupRepository :
  PanacheRepositoryBase<RecipeDraftIngredientGroupEntity, String> {
  fun existsById(id: String): Boolean {
    val params = mapOf("id" to id)
    return count(query = "id = :id", params = params) > 0
  }
}
