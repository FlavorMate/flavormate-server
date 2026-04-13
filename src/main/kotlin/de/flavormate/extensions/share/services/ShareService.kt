/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.share.services

import com.fasterxml.jackson.module.kotlin.readValue
import de.flavormate.configuration.jackson.CustomObjectMapper
import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.core.auth.services.AuthTokenService
import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.importExport.plugins.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.services.IEPluginManager
import de.flavormate.extensions.share.controllers.ShareController
import de.flavormate.extensions.share.mappers.SharedRecipeMapper
import de.flavormate.extensions.urlShortener.services.ShortenerService
import de.flavormate.features.recipe.dtos.mappers.RecipeDtoFullMapper
import de.flavormate.features.recipe.dtos.models.RecipeDtoFull
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.enums.ImageResolution
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.FileService
import de.flavormate.shared.services.TemplateService
import de.flavormate.utils.JSONUtils
import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.StreamingOutput
import jakarta.ws.rs.core.UriBuilder
import org.apache.hc.core5.net.URIBuilder

@RequestScoped
class ShareService(
  private val authorizationDetails: AuthorizationDetails,
  private val recipeRepository: RecipeRepository,
  private val templateService: TemplateService,
  private val shortenerService: ShortenerService,
  private val authTokenService: AuthTokenService,
  private val flavorMateProperties: FlavorMateProperties,
  private val fileService: FileService,
  private val pluginManager: IEPluginManager,
) {

  private val server
    get() = flavorMateProperties.server().url()

  @Location("share/recipe.html") private lateinit var recipeTemplate: Template

  @Transactional
  fun createShareLink(id: String): String {
    val recipeEntity =
      recipeRepository.findById(id) ?: throw FNotFoundException(message = "Recipe not found!")

    val token =
      authTokenService.createAndSaveShareToken(authorizationDetails.getSelf(), recipeEntity)

    val path =
      UriBuilder.fromResource(ShareController::class.java)
        .path(ShareController::class.java, ShareController::shareWeb.name)
        .build(token, recipeEntity.id)
        .toString()

    return shortenerService.generateUrl(path)
  }

  fun shareFileId(id: String, fileId: String, resolution: ImageResolution?): StreamingOutput {
    if (!authTokenService.validateAccess(authorizationDetails.token, id))
      throw FForbiddenException(message = "Token is invalid")

    val recipe =
      recipeRepository.findById(id) ?: throw FNotFoundException(message = "Recipe not found")

    val file =
      recipe.files.find { it.id == fileId } ?: throw FNotFoundException(message = "File not found")

    return fileService.streamFile(
      prefix = FilePath.Recipe,
      uuid = file.id,
      fileName =
        if (file.isTemporary) ImageResolution.Original.path
        else resolution?.path ?: ImageResolution.Original.path,
    )
  }

  fun shareWeb(id: String): TemplateInstance {
    if (!authTokenService.validateAccess(authorizationDetails.token, id))
      throw FForbiddenException(message = "Token is invalid")

    val sharedRecipeMapper = SharedRecipeMapper(templateService)

    val recipeEntity =
      recipeRepository.findById(id) ?: throw FNotFoundException(message = "Recipe not found")

    val images =
      recipeEntity.files.map {
        val path =
          UriBuilder.fromResource(ShareController::class.java)
            .path(ShareController::class.java, ShareController::shareFileId.name)
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

    val appUrl =
      UriBuilder.fromPath("flavormate://")
        .path("open")
        .queryParam("server", flavorMateProperties.server().url())
        .queryParam("type", "recipe")
        .queryParam("id", recipeEntity.id)
        .queryParam("token", authorizationDetails.token)
        .build()
        .toString()

    val data =
      mutableMapOf<String, Any?>(
        "appUrl" to appUrl,
        "recipe" to sharedRecipeMapper.map(recipeEntity, images),
        "ldJson" to JSONUtils.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ldJson),
        "token" to authorizationDetails.token,
      )

    return templateService.handleTemplate(recipeTemplate, data)
  }

  fun openInApp(id: String, language: String): RecipeDtoFull {
    if (!authTokenService.validateAccess(authorizationDetails.token, id))
      throw FForbiddenException(message = "Token is invalid")

    val recipeEntity =
      recipeRepository.findById(id) ?: throw FNotFoundException(message = "Recipe not found")

    return RecipeDtoFullMapper.mapNotNullL10n(input = recipeEntity, language = language)
  }
}
