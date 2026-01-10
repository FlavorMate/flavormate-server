/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.share.services

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.core.auth.services.AuthTokenService
import de.flavormate.exceptions.FForbiddenException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.importExport.ld_json.mappers.LDRecipeRecipeEntityMapper
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

@RequestScoped
class ShareService(
  private val authorizationDetails: AuthorizationDetails,
  private val recipeRepository: RecipeRepository,
  private val sharedRecipeMapper: SharedRecipeMapper,
  private val templateService: TemplateService,
  private val shortenerService: ShortenerService,
  private val authTokenService: AuthTokenService,
  private val flavorMateProperties: FlavorMateProperties,
  private val fileService: FileService,
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

  fun shareFile(id: String, resolution: ImageResolution?): StreamingOutput {
    if (!authTokenService.validateAccess(authorizationDetails.token, id))
      throw FForbiddenException(message = "Token is invalid")

    val recipe =
      recipeRepository.findById(id) ?: throw FNotFoundException(message = "Recipe not found")

    if (recipe.coverFile == null) throw FNotFoundException(message = "Recipe has no cover")

    return fileService.streamFile(
      prefix = FilePath.Recipe,
      uuid = recipe.coverFile!!.id,
      fileName = resolution?.path ?: ImageResolution.Original.path,
    )
  }

  fun shareWeb(id: String): TemplateInstance {
    if (!authTokenService.validateAccess(authorizationDetails.token, id))
      throw FForbiddenException(message = "Token is invalid")

    val recipeEntity =
      recipeRepository.findById(id) ?: throw FNotFoundException(message = "Recipe not found")

    val imagePath =
      UriBuilder.fromResource(ShareController::class.java)
        .path(ShareController::class.java, ShareController::shareFile.name)
        .queryParam("resolution", ImageResolution.Original.name)
        .build(authorizationDetails.token, id)
        .toString()

    val ldJson =
      LDRecipeRecipeEntityMapper.mapNotNullWithToken(
        input = recipeEntity,
        server = server,
        path = imagePath,
      )

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
        "recipe" to sharedRecipeMapper.mapNotNullBasic(recipeEntity),
        "ldJson" to JSONUtils.mapper.writeValueAsString(ldJson),
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
