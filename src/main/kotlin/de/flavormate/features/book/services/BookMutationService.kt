/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.book.services

import de.flavormate.features.book.daos.models.BookEntity
import de.flavormate.features.book.dtos.models.BookCreateDto
import de.flavormate.features.book.repositories.BookRepository
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.shared.services.AuthorizationDetails
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class BookMutationService(
  val bookRepository: BookRepository,
  val recipeRepository: RecipeRepository,
  val authorizationDetails: AuthorizationDetails,
) {
  fun create(form: BookCreateDto): String {
    val self = authorizationDetails.getSelf()
    val book =
      BookEntity.create(account = self, label = form.label).also { bookRepository.persist(it) }

    return book.id
  }
}
