/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.book.services.id

import de.flavormate.features.book.dtos.models.BookUpdateDto
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional

@RequestScoped
class BookIdService(
  private val mutationService: BookIdMutationService,
  private val queryService: BookIdQueryService,
) {
  // GET
  fun getBooksId(id: String) = queryService.getBooksId(id = id)

  fun getBooksIdRecipes(id: String, pagination: Pagination) =
    queryService.getBooksIdRecipes(id = id, pagination = pagination)

  fun getBooksIdSubscribers(id: String, pagination: Pagination) =
    queryService.getBooksIdSubscribers(id = id, pagination = pagination)

  fun getBooksIdSubscriber(id: String) = queryService.getBooksIdSubscriber(id = id)

  fun getBooksIdContainsRecipe(bookId: String, recipeId: String) =
    queryService.getBooksIdContainsRecipe(bookId = bookId, recipeId = recipeId)

  // Mutations
  @Transactional
  fun putBooksIdSubscriber(id: String) = mutationService.putBooksIdSubscriber(id = id)

  @Transactional
  fun putBooksIdRecipe(bookId: String, recipeId: String) =
    mutationService.putBooksIdRecipe(bookId = bookId, recipeId = recipeId)

  @Transactional fun deleteBooksId(id: String) = mutationService.deleteBooksId(id = id)

  @Transactional
  fun putBooksId(id: String, form: BookUpdateDto) = mutationService.putBooksId(id = id, form = form)
}
