/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.bring.services

import com.fasterxml.jackson.module.kotlin.readValue
import de.flavormate.configuration.jackson.CustomObjectMapper
import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.core.auth.services.AuthTokenService
import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.bring.controllers.BringController
import de.flavormate.extensions.importExport.plugins.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.services.IEPluginManager
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.shared.enums.ImageResolution
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
  private val pluginManager: IEPluginManager,
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

    val images =
      recipeEntity.files.map {
        val path =
          UriBuilder.fromResource(BringController::class.java)
            .path(BringController::class.java, BringController::shareFileId.name)
            .build(authorizationDetails.token, id, it.id)
            .toString()
        URIBuilder(server)
          .appendPath(path)
          .addParameter("resolution", ImageResolution.Original.name)
          .toString()
      }

    val ldJsonFile = pluginManager.exportSingle(pluginId = "ld_json", recipe = recipeEntity)

    val ldJson = CustomObjectMapper.instance.readValue<LDJsonRecipe>(ldJsonFile.toFile())

    ldJson.images = images

    val data =
      mutableMapOf<String, Any?>(
        "json" to JSONUtils.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ldJson)
      )

    return templateService.handleTemplate(bringTemplate, data).render()
  }
}
