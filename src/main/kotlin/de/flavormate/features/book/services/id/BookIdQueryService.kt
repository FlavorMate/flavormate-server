/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.book.services.id

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.account.dtos.mappers.AccountPreviewDtoMapper
import de.flavormate.features.account.dtos.models.AccountPreviewDto
import de.flavormate.features.book.dtos.mapper.BookDtoMapper
import de.flavormate.features.book.dtos.models.BookDto
import de.flavormate.features.book.repositories.BookRepository
import de.flavormate.features.recipe.dtos.mappers.RecipeDtoPreviewMapper
import de.flavormate.features.recipe.dtos.models.RecipeDtoPreview
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class BookIdQueryService(
  private val repository: BookRepository,
  private val authorizationDetails: AuthorizationDetails,
) {
  fun getBooksId(id: String): BookDto {
    val entity =
      repository.findById(id = id)
        ?: throw FNotFoundException(message = "Book with id $id not found")

    if (!authorizationDetails.isAdminOrOwner(entity) && !entity.visible)
      throw FForbiddenException(message = "You are not allowed to access this book!")

    return BookDtoMapper.mapNotNullBasic(entity)
  }

  fun getBooksIdRecipes(id: String, pagination: Pagination): PageableDto<RecipeDtoPreview> {
    val entity =
      repository.findById(id = id)
        ?: throw FNotFoundException(message = "Book with id $id not found")

    if (!authorizationDetails.isAdminOrOwner(entity) && !entity.visible)
      throw FForbiddenException(message = "You are not allowed to access this book!")

    val dataQuery =
      repository.findRecipes(id = id, sort = pagination.sortRequest(map = AllowedSorts.recipes))

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = RecipeDtoPreviewMapper::mapNotNullBasic,
    )
  }

  fun getBooksIdSubscribers(id: String, pagination: Pagination): PageableDto<AccountPreviewDto> {
    val entity =
      repository.findById(id = id)
        ?: throw FNotFoundException(message = "Book with id $id not found")

    if (!authorizationDetails.isAdminOrOwner(entity) || !entity.visible)
      throw FForbiddenException(message = "You are not allowed to access this book!")

    val dataQuery =
      repository.findSubscribers(
        id = id,
        sort = pagination.sortRequest(map = AllowedSorts.accounts),
      )

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = AccountPreviewDtoMapper::mapNotNullBasic,
    )
  }

  fun getBooksIdSubscriber(id: String): Boolean {
    val entity =
      repository.findById(id = id)
        ?: throw FNotFoundException(message = "Book with id $id not found")

    if (!authorizationDetails.isAdminOrOwner(entity) && !entity.visible)
      throw FForbiddenException(message = "You are not allowed to access this book!")

    val self = authorizationDetails.getSelf()

    return repository.findSubscriber(id = id, accountId = self.id)
  }

  fun getBooksIdContainsRecipe(bookId: String, recipeId: String): Boolean {
    val book = repository.findById(bookId) ?: throw FNotFoundException(message = "Book not found")

    if (!authorizationDetails.isAdminOrOwner(book) && !book.visible)
      throw FForbiddenException(message = "You are not allowed to access this book!")

    return book.recipes.any { it.id == recipeId }
  }
}
