/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.services

import de.flavormate.features.recipeDraft.dtos.models.update.RecipeDraftUpdateDto
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional

@RequestScoped
class RecipeDraftService(
  private val mutationService: RecipeDraftMutationService,
  private val queryService: RecipeDraftQueryService,
) {

  // GET
  fun getRecipeDrafts(pagination: Pagination) =
    queryService.getRecipeDrafts(pagination = pagination)

  fun getRecipeDraftsId(id: String, language: String) =
    queryService.getRecipeDraftsId(id = id, language = language)

  // POST
  @Transactional fun postRecipeDrafts(): String = mutationService.initializeDraft()

  @Transactional
  fun postRecipeDraftsId(id: String): String = mutationService.createOrUpdateRecipe(id = id)

  @Transactional fun deleteRecipeDraftsId(id: String) = mutationService.delete(id = id)

  @Transactional
  fun putRecipeDraftsId(id: String, form: RecipeDraftUpdateDto) =
    mutationService.updateDraft(id = id, form = form)
}
