/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.ratings.daos

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__recipe__rating")
class RatingEntity {
  @EmbeddedId lateinit var id: RatingEntityId

  @ManyToOne
  @MapsId("accountId")
  @JoinColumn(name = "account_id", insertable = false, updatable = false)
  lateinit var account: AccountEntity

  @ManyToOne
  @MapsId("recipeId")
  @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
  lateinit var recipe: RecipeEntity

  var rating: Double = 0.0

  companion object {
    fun create(account: AccountEntity, recipe: RecipeEntity, rating: Double): RatingEntity {
      val id = RatingEntityId.create(accountId = account.id, recipeId = recipe.id)
      return RatingEntity().apply {
        this.id = id
        this.account = account
        this.recipe = recipe
        this.rating = rating
      }
    }
  }
}
