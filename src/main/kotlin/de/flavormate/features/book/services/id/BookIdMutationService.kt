/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.book.services.id

import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.features.book.dtos.models.BookUpdateDto
import de.flavormate.features.book.repositories.BookRepository
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class BookIdMutationService(
  private val bookRepository: BookRepository,
  private val recipeRepository: RecipeRepository,
  private val authorizationDetails: AuthorizationDetails,
) {
  fun putBooksIdSubscriber(id: String) {
    val book = bookRepository.findById(id) ?: throw FNotFoundException(message = "Book not found!")

    val self = authorizationDetails.getSelf()

    if (book.ownedById != self.id && !book.visible)
      throw FForbiddenException(message = "You are not allowed to subscribe to this book!")

    book.toggleSubscriber(self)

    bookRepository.persist(book)
  }

  fun putBooksIdRecipe(bookId: String, recipeId: String) {
    val book =
      bookRepository.findById(bookId) ?: throw FNotFoundException(message = "Book not found!")

    if (!authorizationDetails.isAdminOrOwner(book))
      throw FForbiddenException(message = "You are not allowed to toggle recipes in this book!")

    val recipe =
      recipeRepository.findById(recipeId) ?: throw FNotFoundException(message = "Recipe not found!")

    book.toggleRecipe(recipe)

    book.setCoverRecipe()

    bookRepository.persist(book)
  }

  fun deleteBooksId(id: String): Boolean {
    val book = bookRepository.findById(id) ?: throw FNotFoundException(message = "Book not found!")

    if (!authorizationDetails.isAdminOrOwner(book))
      throw FForbiddenException(message = "You are not allowed to delete this book!")

    return bookRepository.deleteById(id)
  }

  fun putBooksId(id: String, form: BookUpdateDto) {
    val book = bookRepository.findById(id) ?: throw FNotFoundException(message = "Book not found!")

    if (!authorizationDetails.isAdminOrOwner(book))
      throw FForbiddenException(message = "You are not allowed to update this book!")

    if (form.label != null && form.label.isPresent) {
      book.label = form.label.get()
    }

    if (form.visible != null && form.visible.isPresent) {
      book.visible = form.visible.get()

      // remove all subscribers if book is not visible
      if (!book.visible) {
        book.subscriber.clear()
      }
    }
  }
}
