/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.services

import de.flavormate.exceptions.FBadRequestException
import de.flavormate.extensions.importExport.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonRestrictedDiet
import de.flavormate.features.category.daos.models.CategoryEntity
import de.flavormate.features.category.repositories.CategoryRepository
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftFileEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftServingEntity
import de.flavormate.features.recipeDraft.repositories.RecipeDraftFileRepository
import de.flavormate.features.recipeDraft.repositories.RecipeDraftRepository
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.enums.ImageWideResolution
import de.flavormate.shared.extensions.toKebabCase
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.FileService
import de.flavormate.utils.ImageUtils
import de.flavormate.utils.MimeTypes
import io.quarkus.logging.Log
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import kotlin.io.path.createTempFile
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils

@RequestScoped
class LDJsonService(
  val authorizationDetails: AuthorizationDetails,
  val categoryRepository: CategoryRepository,
  val fileRecipeDraftRepository: RecipeDraftFileRepository,
  val fileService: FileService,
  val recipeDraftRepository: RecipeDraftRepository,
  val instructionService: LDJsonInstructionService,
  val ingredientService: LDJsonIngredientService,
) {

  @Transactional
  fun ldJsonRecipeToRecipeDraftEntity(input: LDJsonRecipe, language: String): RecipeDraftEntity {
    val language = input.inLanguage ?: language

    val recipeDraft =
      RecipeDraftEntity.create(authorizationDetails.getSelf())
        .apply {
          this.label = input.name ?: input.alternateName
          this.description = input.description ?: input.alternativeHeadline
          this.prepTime = input.prepTime
          this.cookTime = input.cookTime
          this.serving =
            input.recipeYield?.let(::mapYield)
              ?: input.yield?.let(::mapYield)
              ?: RecipeDraftServingEntity.create()
          this.instructionGroups =
            instructionService.mapInstructionGroupDrafts(input.recipeInstructions, this)
          this.ingredientGroups =
            ingredientService.mapIngredientGroupDrafts(input.recipeIngredient, language, this)
          this.categories =
            input.recipeCategory.mapNotNullTo(mutableListOf()) { mapCategory(it, language) }
          this.tags =
            input.keywords.filter(StringUtils::isNotBlank).map { it.toKebabCase() }.toMutableList()
          this.diet = mapDiet(input.suitableForDiet)
          this.url = input.url
        }
        .also {
          it.generateIndices()
          recipeDraftRepository.persist(it)

          for (category in it.categories) {
            category.recipeDrafts.add(it)
            categoryRepository.persist(category)
          }
        }

    saveImages(input.images, recipeDraft)

    return recipeDraft
  }

  fun mapCategory(input: String, language: String): CategoryEntity? =
    categoryRepository.findByLocalizedLabel(input)?.also { it.translate(language) }

  fun mapDiet(input: LDJsonRestrictedDiet?): Diet? =
    when (input) {
      LDJsonRestrictedDiet.VegetarianDiet -> Diet.Vegetarian
      LDJsonRestrictedDiet.VeganDiet -> Diet.Vegan
      else -> null
    }

  fun mapYield(input: String): RecipeDraftServingEntity {
    val parts = input.split("\\s+".toRegex()).filter(StringUtils::isNotBlank)
    if (parts.isEmpty()) return RecipeDraftServingEntity.create()

    val amount = parts.first().toDoubleOrNull() ?: return RecipeDraftServingEntity.create()
    if (amount <= 0) return RecipeDraftServingEntity.create()

    val label = parts.drop(1).joinToString(" ")

    return RecipeDraftServingEntity.create().apply {
      this.amount = amount
      this.label = label
    }
  }

  fun saveImages(images: List<String>, recipe: RecipeDraftEntity) {
    for (image in images) {
      var tmpFile: File? = null
      try {
        val uri = URI(image)

        // 1. Validate scheme
        if (uri.scheme != "https") {
          Log.info("Image scraping aborted for $image: No https")
          continue
        }

        // 2. Validate host is not null
        val host = uri.host
        if (host.isNullOrBlank()) {
          Log.info("Image scraping aborted for $image: Invalid host")
          continue
        }

        // 3. Set timeouts and size limits to prevent DoS
        val url = uri.toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 5000 // 5 seconds
        connection.readTimeout = 10000 // 10 seconds
        connection.setRequestProperty("User-Agent", "FlavorMate/3.0")

        // 4. Check content length before downloading
        val contentLength = connection.contentLengthLong
        val maxSize = 10 * 1024 * 1024 // 10MB limit
        if (contentLength > maxSize) {
          Log.info("Image scraping aborted for $image: File too large ($contentLength bytes)")
          connection.disconnect()
          continue
        }

        // 5. Read with size limit
        val bytes =
          connection.inputStream.use { input ->
            val buffer = ByteArray(8192)
            val output = java.io.ByteArrayOutputStream()
            var totalRead = 0L
            var bytesRead: Int

            while (input.read(buffer).also { bytesRead = it } != -1) {
              totalRead += bytesRead
              if (totalRead > maxSize) {
                throw FBadRequestException(message = "File exceeds maximum size limit")
              }
              output.write(buffer, 0, bytesRead)
            }
            output.toByteArray()
          }

        connection.disconnect()

        tmpFile = createTempFile().toFile().also { it.deleteOnExit() }

        FileUtils.writeByteArrayToFile(tmpFile, bytes)

        val entity =
          RecipeDraftFileEntity.create(authorizationDetails.getSelf(), recipe)
            .apply { this.mimeType = MimeTypes.WEBP_MIME }
            .also { fileRecipeDraftRepository.persist(it) }

        val destination = fileService.createPath(FilePath.RecipeDraft, entity.id)

        for (entry in ImageWideResolution.entries) {
          if (entry == ImageWideResolution.Original)
            ImageUtils.scaleMagick(
              tmpFile.toPath(),
              destination.resolve(entry.fileName),
              entry.resolution,
            )
          else
            ImageUtils.resizeMagick(
              tmpFile.toPath(),
              destination.resolve(entry.fileName),
              entry.resolution,
            )
        }
      } catch (e: Exception) {
        Log.error("Failed to download image $image", e)
      } finally {
        tmpFile?.delete()
      }
    }
  }
}
