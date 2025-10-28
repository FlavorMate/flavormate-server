/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.daos.models.rating

import jakarta.persistence.Embeddable
import java.util.*

@Embeddable
class RatingId {
  private lateinit var accountId: String

  private lateinit var recipeId: String

  override fun hashCode(): Int {
    return Objects.hash(accountId, recipeId)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null) return false
    if (javaClass != other.javaClass) return false
    return Objects.hash(accountId, recipeId) == other.hashCode()
  }
}
