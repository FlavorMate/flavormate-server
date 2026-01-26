/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.config.filters.publicFilters

import de.flavormate.core.auth.services.AuthTokenService
import de.flavormate.extensions.bring.controllers.BringController
import jakarta.annotation.Priority
import jakarta.inject.Singleton
import jakarta.ws.rs.ext.Provider
import kotlin.reflect.jvm.javaMethod

@Provider
@Singleton
@Priority(100)
class PublicBringFilter(tokenService: AuthTokenService) : PublicFilter(tokenService) {

  override val securedClass by lazy { BringController::class.java }
  override val securedMethods by lazy {
    listOf(BringController::shareBring.javaMethod, BringController::shareFile.javaMethod)
  }
}
