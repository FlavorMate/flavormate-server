/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.services.id

import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.dtos.mappers.AccountPreviewDtoMapper
import de.flavormate.features.account.dtos.models.AccountPreviewDto
import de.flavormate.features.account.repositories.AccountRepository
import de.flavormate.features.book.dtos.mapper.BookDtoMapper
import de.flavormate.features.book.dtos.models.BookDto
import de.flavormate.features.book.repositories.BookRepository
import de.flavormate.features.recipe.dtos.mappers.RecipeDtoPreviewMapper
import de.flavormate.features.recipe.dtos.models.RecipeDtoPreview
import de.flavormate.features.story.dtos.mappers.StoryDtoPreviewMapper
import de.flavormate.features.story.dtos.models.StoryDtoPreview
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class AccountIdQueryService(
  private val accountRepository: AccountRepository,
  private val authorizationDetails: AuthorizationDetails,
  private val bookRepository: BookRepository,
) {
  fun getAccountsId(id: String): AccountPreviewDto {
    val entity =
      accountRepository.findById(id = id)
        ?: throw FNotFoundException(message = "Account with id $id not found")

    return AccountPreviewDtoMapper.mapNotNullBasic(input = entity)
  }

  fun getAccountsIdBooks(id: String, pagination: Pagination): PageableDto<BookDto> {
    val entity =
      accountRepository.findById(id)
        ?: throw FNotFoundException(message = "Account with id $id not found", id = "")

    val self = authorizationDetails.getSelf()

    val ids =
      listOf(
          entity.books.filter { it.visible || it.ownedById == self.id }.map { it.id },
          entity.subscribedBooks.filter { it.visible || it.ownedById == self.id }.map { it.id },
        )
        .flatten()
        .distinct()

    val dataQuery =
      bookRepository.findByIds(ids = ids, sort = pagination.sortRequest(map = AllowedSorts.books))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = BookDtoMapper::mapNotNullBasic,
    )
  }

  fun getAccountsIdRecipes(id: String, pagination: Pagination): PageableDto<RecipeDtoPreview> {
    val dataQuery =
      accountRepository.findRecipesByAccountId(
        id = id,
        sort = pagination.sortRequest(map = AllowedSorts.recipes),
      )

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = RecipeDtoPreviewMapper::mapNotNullBasic,
    )
  }

  fun getAccountsIdStories(id: String, pagination: Pagination): PageableDto<StoryDtoPreview> {
    val dataQuery =
      accountRepository.findStoriesByAccountId(
        id = id,
        sort = pagination.sortRequest(map = AllowedSorts.stories),
      )

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = StoryDtoPreviewMapper::mapNotNullBasic,
    )
  }
}
