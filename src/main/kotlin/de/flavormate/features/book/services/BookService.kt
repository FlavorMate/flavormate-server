/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.book.services

import de.flavormate.features.book.dtos.models.BookCreateDto
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional

@RequestScoped
class BookService(
  private val mutationService: BookMutationService,
  private val queryService: BookQueryService,
) {
  // GET
  fun getBooks(pagination: Pagination) = queryService.getBooks(pagination = pagination)

  fun getBooksSearch(query: String, pagination: Pagination) =
    queryService.getBooksSearch(query = query, pagination = pagination)

  fun getBooksOwn(pagination: Pagination) = queryService.getBooksOwn(pagination = pagination)

  // Mutations
  @Transactional fun createBook(form: BookCreateDto) = mutationService.create(form = form)
}
