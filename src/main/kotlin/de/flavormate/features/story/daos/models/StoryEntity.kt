/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.story.daos.models

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.shared.models.entities.OwnedEntity
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "v3__story")
class StoryEntity : OwnedEntity() {
  lateinit var content: String

  lateinit var label: String

  @ManyToOne
  @JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false)
  lateinit var recipe: RecipeEntity

  companion object {
    fun create(account: AccountEntity): StoryEntity {
      return StoryEntity().apply {
        this.ownedBy = account
        this.ownedById = account.id
      }
    }
  }
}
