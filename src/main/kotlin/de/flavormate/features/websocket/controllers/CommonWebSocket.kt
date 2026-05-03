/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.websocket.controllers

import io.quarkus.security.Authenticated
import io.quarkus.websockets.next.OnClose
import io.quarkus.websockets.next.OnOpen
import io.quarkus.websockets.next.WebSocket

@WebSocket(path = "/v3/websocket/common")
@Authenticated
class CommonWebSocket {
  @OnOpen fun onOpen() {}

  @OnClose fun onClose() {}
}
