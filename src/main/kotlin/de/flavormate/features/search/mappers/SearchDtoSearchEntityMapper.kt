/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.search.mappers

import de.flavormate.features.account.controllers.AccountController
import de.flavormate.features.recipe.controllers.RecipeController
import de.flavormate.features.search.models.SearchDto
import de.flavormate.features.search.models.SearchEntity
import de.flavormate.features.search.models.SearchFilter
import de.flavormate.shared.interfaces.BasicMapper
import jakarta.ws.rs.core.UriBuilder

object SearchDtoSearchEntityMapper : BasicMapper<SearchEntity, SearchDto>() {
  override fun mapNotNullBasic(input: SearchEntity): SearchDto {
    val coverUrl =
      if (input.fileId != null)
        when (input.source) {
          SearchFilter.Account ->
            UriBuilder.fromResource(AccountController::class.java)
              .path(AccountController::class.java, AccountController::getAccountsAvatar.name)
              .build(input.entityId, input.fileId)
              .toString()

          else ->
            UriBuilder.fromResource(RecipeController::class.java)
              .path(RecipeController::class.java, RecipeController::getRecipesFilesId.name)
              .build(input.recipeId, input.fileId)
              .toString()
        }
      else null

    return SearchDto(
      id = input.entityId,
      source = input.source,
      label = input.label,
      cover = coverUrl,
    )
  }
}
