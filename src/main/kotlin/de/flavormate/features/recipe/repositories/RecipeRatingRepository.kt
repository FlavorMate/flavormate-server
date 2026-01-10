/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.repositories

import de.flavormate.extensions.ratings.daos.RatingEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecipeRatingRepository : PanacheRepositoryBase<RatingEntity, String> {
  fun findByAccountAndRecipe(accountId: String, recipeId: String): RatingEntity? {
    val params = mapOf("accountId" to accountId, "recipeId" to recipeId)
    return find(query = "id.accountId = :accountId and id.recipeId = :recipeId", params = params)
      .firstResult()
  }

  fun findByRecipeId(recipeId: String): List<RatingEntity> {
    val params = mapOf("recipeId" to recipeId)
    return list(query = "id.recipeId = :recipeId", params = params)
  }

  fun deleteByAccountAndRecipe(accountId: String, recipeId: String): Long {
    val params = mapOf("accountId" to accountId, "recipeId" to recipeId)
    return delete(query = "id.accountId = :accountId and id.recipeId = :recipeId", params = params)
  }
}
