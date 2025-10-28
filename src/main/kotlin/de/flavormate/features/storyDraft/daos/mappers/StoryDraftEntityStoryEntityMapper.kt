/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.storyDraft.daos.mappers

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.story.daos.models.StoryEntity
import de.flavormate.features.storyDraft.daos.models.StoryDraftEntity
import de.flavormate.shared.interfaces.OwnedMapper

object StoryDraftEntityStoryEntityMapper : OwnedMapper<StoryEntity, StoryDraftEntity>() {
  override fun mapNotNullOwned(input: StoryEntity, account: AccountEntity): StoryDraftEntity {
    return StoryDraftEntity().apply {
      this.ownedBy = account.ownedBy
      this.ownedById = account.ownedById
      this.originId = input.id
      this.label = input.label
      this.content = input.content
      this.recipe = input.recipe
      //      this.recipe = StoryDraftRecipeEntityRecipeEntityMapper.mapNotNullBasic(input.recipe)
    }
  }
}
