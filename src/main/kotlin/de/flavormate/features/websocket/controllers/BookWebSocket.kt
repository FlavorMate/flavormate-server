/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.websocket.controllers

import de.flavormate.features.book.repositories.BookRepository
import de.flavormate.shared.services.AuthorizationDetails
import io.quarkus.security.Authenticated
import io.quarkus.websockets.next.*
import jakarta.enterprise.context.RequestScoped

@WebSocket(path = "/v3/websocket/book/{id}")
@RequestScoped
@Authenticated
class BookWebSocket(
  private val authorizationDetails: AuthorizationDetails,
  private val bookRepository: BookRepository,
  private val connection: WebSocketConnection,
) {
  private val id
    get() = connection.pathParam("id")

  @OnOpen
  fun onOpen() {
    val authorized = bookRepository.isBookAuthorized(authorizationDetails.subject, id)

    if (!authorized) {
      connection.closeAndAwait(CloseReason(401, "Unauthorized"))
    }
  }

  @OnClose fun onClose() {}
}
