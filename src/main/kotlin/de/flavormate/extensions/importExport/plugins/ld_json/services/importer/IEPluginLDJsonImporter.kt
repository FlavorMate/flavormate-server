/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ld_json.services.importer

import de.flavormate.exceptions.FBadRequestException
import de.flavormate.extensions.importExport.interfaces.IEPluginContext
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraft
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraftServing
import de.flavormate.extensions.importExport.plugins.ld_json.mappers.LDJsonIngredientMapper
import de.flavormate.extensions.importExport.plugins.ld_json.mappers.LDJsonInstructionMapper
import de.flavormate.extensions.importExport.plugins.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.LDJsonRestrictedDiet
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.enums.Language
import de.flavormate.shared.extensions.toKebabCase
import io.quarkus.logging.Log
import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import kotlin.io.path.createTempFile
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils

class IEPluginLDJsonImporter(private val context: IEPluginContext) {

  fun import(input: LDJsonRecipe): IERecipeDraft {
    val lang = input.inLanguage?.let { Language.from(it) } ?: Language.EN

    return IERecipeDraft(
      cookTime = input.cookTime,
      course = null,
      description = input.description ?: input.alternativeHeadline,
      diet = mapDiet(input.suitableForDiet),
      label = input.name ?: input.alternateName,
      prepTime = input.prepTime,
      restTime = null,
      serving = mapServing(input.recipeYield ?: input.yield),
      instructionGroups = LDJsonInstructionMapper.mapInstructionGroups(input.recipeInstructions),
      ingredientGroups = LDJsonIngredientMapper.mapIngredientGroups(input.recipeIngredient),
      categories = input.recipeCategory.filter(StringUtils::isNotBlank),
      tags = input.keywords.filter(StringUtils::isNotBlank).map { it.toKebabCase() },
      files = input.images.mapNotNull { saveImage(it) },
      url = input.url,
      language = lang,
    )
  }

  private fun mapDiet(input: LDJsonRestrictedDiet?): Diet? =
    when (input) {
      LDJsonRestrictedDiet.VegetarianDiet -> Diet.Vegetarian
      LDJsonRestrictedDiet.VeganDiet -> Diet.Vegan
      else -> null
    }

  private fun mapServing(input: String?): IERecipeDraftServing {
    if (input == null) return IERecipeDraftServing.empty()

    val parts = input.split("\\s+".toRegex()).filter(StringUtils::isNotBlank)
    if (parts.isEmpty()) return IERecipeDraftServing.empty()

    val amount = parts.first().toDoubleOrNull() ?: return IERecipeDraftServing.empty()
    if (amount <= 0) return IERecipeDraftServing.empty()

    val label = parts.drop(1).joinToString(" ")

    return IERecipeDraftServing(amount = amount, label = label)
  }

  private fun saveImage(image: String): File? {
    var tmpFile: File?

    try {
      val uri = URI(image)

      // 1. Validate scheme
      if (uri.scheme != "https") {
        Log.info("Image scraping aborted for $image: No https")
        return null
      }

      // 2. Validate host is not null
      val host = uri.host
      if (host.isNullOrBlank()) {
        Log.info("Image scraping aborted for $image: Invalid host")
        return null
      }

      // 3. Set timeouts and size limits to prevent DoS
      val url = uri.toURL()
      val connection = url.openConnection() as HttpURLConnection
      connection.connectTimeout = 5000 // 5 seconds
      connection.readTimeout = 10000 // 10 seconds
      connection.setRequestProperty("User-Agent", "FlavorMate/3.0")

      // 4. Check content length before downloading
      val contentLength = connection.contentLengthLong
      if (contentLength > context.maxImageSize.asLongValue()) {
        Log.info("Image scraping aborted for $image: File too large ($contentLength bytes)")
        connection.disconnect()
        return null
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
            if (totalRead > context.maxImageSize.asLongValue()) {
              throw FBadRequestException(message = "File exceeds maximum size limit")
            }
            output.write(buffer, 0, bytesRead)
          }
          output.toByteArray()
        }

      connection.disconnect()

      tmpFile = createTempFile().toFile().also { it.deleteOnExit() }

      FileUtils.writeByteArrayToFile(tmpFile, bytes)

      return tmpFile
    } catch (e: Exception) {
      Log.error("Failed to download image $image", e)
      return null
    }
  }
}
