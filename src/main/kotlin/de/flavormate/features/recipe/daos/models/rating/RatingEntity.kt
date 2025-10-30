/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.daos.models.rating

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__recipe__rating")
class RatingEntity {
  @EmbeddedId lateinit var id: RatingId

  @ManyToOne
  @MapsId("accountId")
  @JoinColumn(name = "account_id")
  lateinit var account: AccountEntity

  @ManyToOne @MapsId("recipeId") @JoinColumn(name = "recipe_id") lateinit var recipe: RecipeEntity

  var rating: Double = 0.0
}
