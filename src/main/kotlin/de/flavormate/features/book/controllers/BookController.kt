/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.book.controllers

import de.flavormate.features.book.dtos.models.BookCreateDto
import de.flavormate.features.book.dtos.models.BookUpdateDto
import de.flavormate.features.book.services.BookService
import de.flavormate.features.book.services.id.BookIdService
import de.flavormate.shared.models.api.Pagination
import jakarta.enterprise.context.RequestScoped
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.ws.rs.*
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestQuery

@RequestScoped
@Path("/v3/books")
class BookController(
  private val bookService: BookService,
  private val bookIdService: BookIdService,
) {
  @GET
  fun getBooks(@BeanParam pagination: Pagination) = bookService.getBooks(pagination = pagination)

  @POST fun createBook(@NotNull form: BookCreateDto) = bookService.createBook(form = form)

  @GET
  @Path("/search")
  fun getBooksSearch(@RestQuery @NotBlank query: String, @BeanParam pagination: Pagination) =
    bookService.getBooksSearch(query = query, pagination = pagination)

  @GET
  @Path("/own")
  fun getBooksOwn(@BeanParam pagination: Pagination) =
    bookService.getBooksOwn(pagination = pagination)

  // ID
  @GET @Path("/{id}") fun getBooksId(@RestPath id: String) = bookIdService.getBooksId(id = id)

  @DELETE
  @Path("/{id}")
  fun deleteBooksId(@RestPath id: String) = bookIdService.deleteBooksId(id = id)

  @PUT
  @Path("/{id}")
  fun putBooksId(@RestPath("id") @NotBlank id: String, @NotNull form: BookUpdateDto) =
    bookIdService.putBooksId(id = id, form = form)

  @GET
  @Path("/{bookId}/recipes/{recipeId}")
  fun getBooksIdContainsRecipe(@RestPath bookId: String, @RestPath recipeId: String) =
    bookIdService.getBooksIdContainsRecipe(bookId = bookId, recipeId = recipeId)

  // ID - Children
  @GET
  @Path("/{id}/recipes")
  fun getBooksIdRecipes(@RestPath id: String, @BeanParam pagination: Pagination) =
    bookIdService.getBooksIdRecipes(id = id, pagination = pagination)

  @GET
  @Path("/{id}/subscribers")
  fun getBooksIdSubscribers(@RestPath id: String, @BeanParam pagination: Pagination) =
    bookIdService.getBooksIdSubscribers(id = id, pagination = pagination)

  @GET
  @Path("/{id}/subscriber")
  fun getBooksIdSubscriber(@RestPath id: String) = bookIdService.getBooksIdSubscriber(id = id)

  @PUT
  @Path("/{id}/subscriber")
  fun putBooksIdSubscriber(@RestPath id: String) = bookIdService.putBooksIdSubscriber(id = id)

  @PUT
  @Path("/{bookId}/recipes/{recipeId}")
  fun putBooksIdRecipe(@RestPath bookId: String, @RestPath recipeId: String) =
    bookIdService.putBooksIdRecipe(bookId = bookId, recipeId = recipeId)
}
