/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.storyDraft.services

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.features.story.daos.models.StoryEntity
import de.flavormate.features.story.repositories.StoryRepository
import de.flavormate.features.storyDraft.daos.models.StoryDraftEntity
import de.flavormate.features.storyDraft.dtos.models.StoryDraftUpdateDto
import de.flavormate.features.storyDraft.repositories.StoryDraftRepository
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped
import kotlin.jvm.optionals.getOrNull

@RequestScoped
class StoryDraftMutationService(
  private val storyRepository: StoryRepository,
  private val recipeRepository: RecipeRepository,
  private val storyDraftRepository: StoryDraftRepository,
  private val authorizationDetails: AuthorizationDetails,
) {
  fun postStoryDrafts(): String {
    val self = authorizationDetails.getSelf()

    val draft = StoryDraftEntity.create(account = self).also { storyDraftRepository.persist(it) }

    return draft.id
  }

  // ID

  fun deleteStoryDraftsId(id: String): Boolean {
    val story =
      storyDraftRepository.findById(id) ?: throw FNotFoundException(message = "Story not found!")

    if (!authorizationDetails.isAdminOrOwner(story))
      throw FForbiddenException(message = "You are not allowed to delete this story!")

    return storyDraftRepository.deleteById(id)
  }

  fun putStoryDraftsId(id: String, form: StoryDraftUpdateDto) {
    val entity =
      storyDraftRepository.findById(id)
        ?: throw FNotFoundException(message = "Story draft not found!")

    if (!authorizationDetails.isAdminOrOwner(entity))
      throw FForbiddenException(message = "You are not allowed to update this story draft!")

    form.label?.let { entity.label = it.getOrNull() }
    form.content?.let { entity.content = it.getOrNull() }
    form.recipe?.let {
      val recipe = it.getOrNull()?.let { recipeId -> recipeRepository.findById(recipeId) }

      entity.recipe = recipe
    }

    storyDraftRepository.persist(entity)
  }

  fun postStoryDraftsId(id: String): String {

    val account = authorizationDetails.getSelf()

    val draftEntity =
      storyDraftRepository.findById(id)
        ?: throw FNotFoundException(message = "Story draft not found!")

    if (!authorizationDetails.isAdminOrOwner(draftEntity))
      throw FForbiddenException(message = "You are not allowed to create a story from this draft!")

    val story =
      if (draftEntity.originId == null) createStory(account, draftEntity)
      else updateStory(draftEntity)

    storyDraftRepository.deleteById(id)

    return story.id
  }

  private fun createStory(account: AccountEntity, draftEntity: StoryDraftEntity): StoryEntity {
    val recipe =
      recipeRepository.findById(draftEntity.recipe!!.id)
        ?: throw FNotFoundException(message = "Recipe not found!")

    return StoryEntity.create(account)
      .apply {
        this.label = draftEntity.label!!
        this.content = draftEntity.content!!
        this.recipe = recipe
      }
      .also { storyRepository.persist(it) }
  }

  private fun updateStory(draftEntity: StoryDraftEntity): StoryEntity {
    val story =
      storyRepository.findById(draftEntity.originId!!)
        ?: throw FNotFoundException(message = "Story not found!")

    val recipe =
      recipeRepository.findById(draftEntity.recipe!!.id)
        ?: throw FNotFoundException(message = "Recipe not found!")

    return story
      .apply {
        this.label = draftEntity.label!!
        this.content = draftEntity.content!!
        this.recipe = recipe
      }
      .also { storyRepository.persist(it) }
  }
}
