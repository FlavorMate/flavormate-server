/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.story.services

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.story.repositories.StoryRepository
import de.flavormate.features.storyDraft.daos.mappers.StoryDraftEntityStoryEntityMapper
import de.flavormate.features.storyDraft.repositories.StoryDraftRepository
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class StoryMutationService(
  private val storyRepository: StoryRepository,
  private val storyDraftRepository: StoryDraftRepository,
  private val authorizationDetails: AuthorizationDetails,
) {

  fun deleteStoriesId(id: String): Boolean {
    val story = storyRepository.findById(id) ?: throw FNotFoundException("Story not found!")

    if (!authorizationDetails.isAdminOrOwner(story))
      throw FForbiddenException(message = "You are not allowed to delete this story!")

    return storyRepository.deleteById(id)
  }

  // Returns the created draft id
  fun editStoriesId(id: String): String {
    val story =
      storyRepository.findById(id) ?: throw FNotFoundException(message = "Story not found!")

    if (!authorizationDetails.isAdminOrOwner(story))
      throw FForbiddenException(message = "You are not allowed to create a draft for this story!")

    val originEntity = storyDraftRepository.findByOriginId(id)

    if (originEntity != null) return originEntity.id

    val self = authorizationDetails.getSelf()

    val entity =
      StoryDraftEntityStoryEntityMapper.mapNotNullOwned(input = story, account = self).also {
        storyDraftRepository.persist(it)
      }

    return entity.id
  }
}
