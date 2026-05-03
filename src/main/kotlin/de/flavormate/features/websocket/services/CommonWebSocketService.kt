/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.websocket.services

import de.flavormate.features.websocket.controllers.CommonWebSocket
import de.flavormate.features.websocket.enums.CommonWebSocketType
import io.quarkus.websockets.next.OpenConnections
import jakarta.enterprise.context.ApplicationScoped
import java.time.Duration

@ApplicationScoped
class CommonWebSocketService(private val connections: OpenConnections) {
  fun sendMessage(category: CommonWebSocketType) {
    val connection =
      connections.findByEndpointId(CommonWebSocket::class.java.name).firstOrNull() ?: return

    val broadcast = connection.broadcast()

    broadcast.sendText(category.name).await().atMost(Duration.ofSeconds(5))
  }
}
