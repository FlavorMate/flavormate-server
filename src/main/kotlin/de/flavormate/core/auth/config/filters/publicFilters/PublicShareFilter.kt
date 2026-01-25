/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.config.filters.publicFilters

import de.flavormate.core.auth.services.AuthTokenService
import de.flavormate.extensions.share.controllers.ShareController
import jakarta.annotation.Priority
import jakarta.inject.Singleton
import jakarta.ws.rs.ext.Provider
import kotlin.reflect.jvm.javaMethod

@Provider
@Singleton
@Priority(100)
class PublicShareFilter(tokenService: AuthTokenService) : PublicFilter(tokenService) {

  override val securedClass by lazy { ShareController::class.java }
  override val securedMethods by lazy {
    listOf(
      ShareController::shareWeb.javaMethod,
      ShareController::shareFile.javaMethod,
      ShareController::openInApp.javaMethod,
    )
  }
}
