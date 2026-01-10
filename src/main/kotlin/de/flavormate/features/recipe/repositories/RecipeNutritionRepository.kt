/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.repositories

import de.flavormate.features.recipe.daos.models.NutritionEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecipeNutritionRepository : PanacheRepositoryBase<NutritionEntity, String> {
  fun deleteOrphan(): Long {
    return delete("ingredient is null")
  }
}
