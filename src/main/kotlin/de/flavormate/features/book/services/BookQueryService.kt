/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.book.services

import de.flavormate.features.book.dtos.mapper.BookDtoMapper
import de.flavormate.features.book.dtos.models.BookDto
import de.flavormate.features.book.repositories.BookRepository
import de.flavormate.shared.constants.AllowedSorts
import de.flavormate.shared.models.api.PageableDto
import de.flavormate.shared.models.api.Pagination
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class BookQueryService(
  private val repository: BookRepository,
  private val authorizationDetails: AuthorizationDetails,
) {
  fun getBooks(pagination: Pagination): PageableDto<BookDto> {
    val self = authorizationDetails.getSelf()

    val dataQuery =
      repository.findOwnView(id = self.id, sort = pagination.sortRequest(map = AllowedSorts.books))

    val countQuery = repository.countOwnView(id = self.id)

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      countQuery = countQuery,
      page = pagination.pageRequest,
      mapper = BookDtoMapper::mapNotNullBasic,
    )
  }

  fun getBooksOwn(pagination: Pagination): PageableDto<BookDto> {
    val self = authorizationDetails.getSelf()

    val dataQuery =
      repository.findOwn(id = self.id, sort = pagination.sortRequest(map = AllowedSorts.books))

    val countQuery = repository.countOwn(id = self.id)

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      countQuery = countQuery,
      page = pagination.pageRequest,
      mapper = BookDtoMapper::mapNotNullBasic,
    )
  }

  fun getBooksSearch(query: String, pagination: Pagination): PageableDto<BookDto> {
    val self = authorizationDetails.getSelf()

    val dataQuery =
      repository.findBySearch(
        query = query,
        accountId = self.id,
        sort = pagination.sortRequest(AllowedSorts.books),
      )

    return PageableDto.fromQuery(
      dataQuery = dataQuery,
      page = pagination.pageRequest,
      mapper = BookDtoMapper::mapNotNullBasic,
    )
  }
}
