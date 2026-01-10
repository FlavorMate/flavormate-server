/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.ratings.daos

import jakarta.persistence.Embeddable
import java.util.*

@Embeddable
class RatingEntityId {
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

  companion object {
    fun create(accountId: String, recipeId: String): RatingEntityId {
      return RatingEntityId().apply {
        this.accountId = accountId
        this.recipeId = recipeId
      }
    }
  }
}
