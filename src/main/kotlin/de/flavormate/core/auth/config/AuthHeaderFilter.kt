/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.config

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.extensions.bring.controllers.BringController
import de.flavormate.extensions.recovery.controllers.RecoveryController
import de.flavormate.extensions.registration.controllers.RegistrationController
import de.flavormate.extensions.share.controllers.ShareController
import io.vertx.ext.web.Router
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.UriBuilder

/**
 * A class responsible for intercepting HTTP requests and adding an authorization header when it is
 * missing. This is used to handle scenarios where tokens are included in the URL path and need to
 * be extracted and injected as a `Bearer` token in the `Authorization` header.
 *
 * This filter operates with a lower order than `HttpAuthenticationMechanism`, ensuring its logic is
 * executed earlier.
 *
 * Token extraction is performed by parsing the request path based on the predefined indices for
 * each route, and the extracted token (if present) is appended to the `Authorization` header before
 * proceeding with the request.
 */
@ApplicationScoped
class AuthHeaderFilter(private val flavorMateProperties: FlavorMateProperties) {

  companion object {

    /** The order used by `HttpAuthenticationMechanism` is -150 so this one has to be lower */
    private const val FILTER_ORDER = -200

    private val BRING_PATH = UriBuilder.fromResource(BringController::class.java).build().toString()
    private const val BRING_INDEX = 3

    private val RECOVERY_PATH =
      UriBuilder.fromResource(RecoveryController::class.java).build().toString()
    private const val RECOVERY_INDEX = 5

    private val REGISTRATION_PATH =
      UriBuilder.fromResource(RegistrationController::class.java).build().toString()
    private const val REGISTRATION_INDEX = 4

    private val SHARE_PATH = UriBuilder.fromResource(ShareController::class.java).build().toString()
    private const val SHARE_INDEX = 3
  }

  private val serverPathPrefix by lazy {
    flavorMateProperties.server().path().takeUnless { it == "/" } ?: ""
  }

  fun setupFilter(@Observes router: Router) {
    router.route().order(FILTER_ORDER).handler { rc ->
      val request = rc.request()

      val hasAuthorization = !request.getHeader(HttpHeaders.AUTHORIZATION).isNullOrBlank()

      if (!hasAuthorization) {
        val path = request.path()

        val token =
          when {
            path.startsWith(BRING_PATH) -> extractPathSegment(path, BRING_INDEX)
            path.startsWith(RECOVERY_PATH) -> extractPathSegment(path, RECOVERY_INDEX)
            path.startsWith(REGISTRATION_PATH) -> extractPathSegment(path, REGISTRATION_INDEX)
            path.startsWith(SHARE_PATH) -> extractPathSegment(path, SHARE_INDEX)
            else -> null
          }

        token?.let { request.headers().add(HttpHeaders.AUTHORIZATION, "Bearer $it") }
      }

      rc.next()
    }
  }

  private fun extractPathSegment(path: String, index: Int): String? =
    path.removePrefix(serverPathPrefix).split("/").getOrNull(index)
}
