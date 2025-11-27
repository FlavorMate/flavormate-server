/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.auth.config

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.core.features.enums.FeatureType
import de.flavormate.extensions.bring.controllers.BringController
import de.flavormate.extensions.ratings.controllers.RatingsController
import de.flavormate.extensions.recovery.controllers.RecoveryController
import de.flavormate.extensions.registration.controllers.RegistrationController
import de.flavormate.extensions.share.controllers.ShareController
import de.flavormate.features.story.controllers.StoryController
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Path
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider

@Provider
@ApplicationScoped
class FeatureRequestFilter(private val flavorMateProperties: FlavorMateProperties) :
  ContainerRequestFilter {
  private val bringEnabled = flavorMateProperties.features()[FeatureType.Bring]!!.enabled()
  private val bringPath = BringController::class.java.getAnnotation(Path::class.java).value

  private val recoveryEnabled = flavorMateProperties.features()[FeatureType.Recovery]!!.enabled()
  private val recoveryPath = RecoveryController::class.java.getAnnotation(Path::class.java).value

  private val registrationEnabled =
    flavorMateProperties.features()[FeatureType.Registration]!!.enabled()
  private val registrationPath =
    RegistrationController::class.java.getAnnotation(Path::class.java).value

  private val ratingsEnabled = flavorMateProperties.features()[FeatureType.Ratings]!!.enabled()
  private val ratingsPath = RatingsController::class.java.getAnnotation(Path::class.java).value

  private val shareEnabled = flavorMateProperties.features()[FeatureType.Share]!!.enabled()
  private val sharePath = ShareController::class.java.getAnnotation(Path::class.java).value

  private val storyEnabled = flavorMateProperties.features()[FeatureType.Story]!!.enabled()
  private val storyPath = StoryController::class.java.getAnnotation(Path::class.java).value

  override fun filter(ctx: ContainerRequestContext?) {
    if (ctx == null) return

    checkBringFeature(ctx)
    checkRatingsFeature(ctx)
    checkRecoveryFeature(ctx)
    checkRegistrationFeature(ctx)
    checkShareFeature(ctx)
    checkStoryFeature(ctx)
  }

  fun checkBringFeature(ctx: ContainerRequestContext) {
    val requestedPath = ctx.uriInfo.requestUri.path

    if (!requestedPath.startsWith(bringPath)) return

    if (!bringEnabled) ctx.abortWith(Response.status(Response.Status.NOT_FOUND).build())
  }

  fun checkRatingsFeature(ctx: ContainerRequestContext) {
    val requestedPath = ctx.uriInfo.requestUri.path

    if (!requestedPath.startsWith(ratingsPath)) return

    if (!ratingsEnabled) ctx.abortWith(Response.status(Response.Status.NOT_FOUND).build())
  }

  fun checkRecoveryFeature(ctx: ContainerRequestContext) {
    val requestedPath = ctx.uriInfo.requestUri.path

    if (!requestedPath.startsWith(recoveryPath)) return

    if (!recoveryEnabled) ctx.abortWith(Response.status(Response.Status.NOT_FOUND).build())
  }

  fun checkRegistrationFeature(ctx: ContainerRequestContext) {
    val requestedPath = ctx.uriInfo.requestUri.path

    if (!requestedPath.startsWith(registrationPath)) return

    if (!registrationEnabled) ctx.abortWith(Response.status(Response.Status.NOT_FOUND).build())
  }

  fun checkShareFeature(ctx: ContainerRequestContext) {
    val requestedPath = ctx.uriInfo.requestUri.path

    if (!requestedPath.startsWith(sharePath)) return

    if (!shareEnabled) ctx.abortWith(Response.status(Response.Status.NOT_FOUND).build())
  }

  fun checkStoryFeature(ctx: ContainerRequestContext) {
    val requestedPath = ctx.uriInfo.requestUri.path

    if (!requestedPath.startsWith(storyPath)) return

    if (!storyEnabled) ctx.abortWith(Response.status(Response.Status.NOT_FOUND).build())
  }
}
