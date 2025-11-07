/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.bring.services

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.core.auth.services.AuthTokenService
import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.bring.controllers.BringController
import de.flavormate.extensions.importExport.ld_json.mappers.LDRecipeRecipeEntityMapper
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.shared.enums.ImageWideResolution
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.TemplateService
import de.flavormate.utils.JSONUtils
import io.quarkus.qute.Location
import io.quarkus.qute.Template
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.UriBuilder
import org.apache.hc.core5.net.URIBuilder

@RequestScoped
class BringService(
  private val authorizationDetails: AuthorizationDetails,
  private val authTokenService: AuthTokenService,
  private val flavorMateProperties: FlavorMateProperties,
  private val recipeRepository: RecipeRepository,
  private val tokenService: AuthTokenService,
  private val templateService: TemplateService,
) {

  private val server
    get() = flavorMateProperties.server().url()

  @Location("share/bring.html") private lateinit var bringTemplate: Template

  @Transactional
  fun getBringUrl(recipeId: String): String {
    val recipe =
      recipeRepository.findById(recipeId) ?: throw FNotFoundException(message = "Recipe not found")

    val token = tokenService.createAndSaveBringToken(authorizationDetails.getSelf(), recipe)

    val path =
      UriBuilder.fromResource(BringController::class.java)
        .path(BringController::class.java, BringController::shareBring.name)
        .build(token, recipe.id)
        .toString()

    return URIBuilder(server).appendPath(path).toString()
  }

  fun shareBring(id: String): String {
    if (!authTokenService.validateAccess(authorizationDetails.token, id))
      throw FForbiddenException(message = "Token is invalid")

    val recipeEntity =
      recipeRepository.findById(id) ?: throw FNotFoundException(message = "Recipe not found")

    val imagePath =
      UriBuilder.fromResource(BringController::class.java)
        .path(BringController::class.java, BringController::shareFile.name)
        .queryParam("resolution", ImageWideResolution.Original.name)
        .build(authorizationDetails.token, id)
        .toString()

    val ldJson =
      LDRecipeRecipeEntityMapper.mapNotNullWithToken(
        input = recipeEntity,
        server = server,
        path = imagePath,
      )

    val data = mutableMapOf<String, Any?>("json" to JSONUtils.mapper.writeValueAsString(ldJson))

    return templateService.handleTemplate(bringTemplate, data).render()
  }
}
