/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.storyDraft.daos.models

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.shared.models.entities.OwnedEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__story_draft")
class StoryDraftEntity : OwnedEntity() {
  var label: String? = null

  var content: String? = null

  @ManyToOne
  @JoinColumn(name = "recipe", referencedColumnName = "id")
  var recipe: RecipeEntity? = null

  @Column(name = "origin_id") var originId: String? = null

  companion object {
    fun create(account: AccountEntity): StoryDraftEntity {
      return StoryDraftEntity().apply {
        this.ownedBy = account.ownedBy
        this.ownedById = account.ownedById
      }
    }
  }
}
