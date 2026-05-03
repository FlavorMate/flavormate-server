/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.websocket.services

import de.flavormate.features.websocket.controllers.BookWebSocket
import io.quarkus.websockets.next.OpenConnections
import jakarta.enterprise.context.ApplicationScoped
import java.time.Duration

@ApplicationScoped
class BookWebSocketService(private val connections: OpenConnections) {
  fun sendMessage(id: String) {
    println("Sending message to $id")
    connections
      .findByEndpointId(BookWebSocket::class.java.name)
      .filter { it.pathParam("id") == id }
      .forEach {
        it.sendText("Updated").await().atMost(Duration.ofSeconds(5))
        println("Sending message to ${it.pathParam("id")}")
      }
  }
}
