/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.nextcloud_cookbook.services

import de.flavormate.exceptions.FBadRequestException
import de.flavormate.extensions.importExport.interfaces.IEPlugin
import de.flavormate.extensions.importExport.models.IEPluginContext
import de.flavormate.extensions.importExport.models.IEPluginMetadata
import de.flavormate.extensions.importExport.models.ieRecipe.IERecipe
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraft
import de.flavormate.extensions.importExport.models.inputSource.FileInputSource
import de.flavormate.extensions.importExport.models.inputSource.IEImportType
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import de.flavormate.extensions.importExport.plugins.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.plugins.nextcloud_cookbook.services.exporter.IEPluginLDNextcloudCookbookExporter
import de.flavormate.extensions.importExport.plugins.nextcloud_cookbook.services.importer.IEPluginNextcloudCookbookImporter
import de.flavormate.shared.enums.Language
import de.flavormate.shared.services.LanguageDetectorService
import de.flavormate.utils.ZipUtils
import jakarta.enterprise.context.ApplicationScoped
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries

@ApplicationScoped
class IEPluginNextcloudCookbook(private val languageDetectorService: LanguageDetectorService) :
  IEPlugin {

  override val metadata =
    IEPluginMetadata(
      id = "nextcloud_cookbook",
      name = mapOf(Language.EN to "Nextcloud Cookbook", Language.DE to "Nextcloud Kochbuch"),
      version = "1.0.0",
      author = "FlavorMate",
      description =
        mapOf(
          Language.EN to "Import and Export recipes from the Nextcloud Cookbook App",
          Language.DE to "Import und Export von Rezepten aus der Nextcloud Kochbuch App",
        ),
      import = listOf(IEImportType.FileImport),
      export = true,
      supportedMimeTypes = listOf("application/zip"),
      supportedExtensions = listOf("zip"),
    )

  override fun importSingle(
    input: IEInputSource,
    workDirectory: Path,
    context: IEPluginContext,
  ): IERecipeDraft {
    throw FBadRequestException("Importing single files is not supported by ${metadata.name}")
  }

  override fun importMultiple(
    inputs: List<IEInputSource>,
    workDirectory: Path,
    context: IEPluginContext,
  ): List<IERecipeDraft> {
    return inputs.flatMap { input ->
      if (metadata.import.none { it.isImportSupported(input) }) {
        throw FBadRequestException(
          message = "Unsupported import type ${input::class.simpleName} for ${metadata.name}"
        )
      }
      val zipFile =
        when (input) {
          is FileInputSource -> input.file.toPath()
          else ->
            throw FBadRequestException(
              "Unsupported import type ${input::class.simpleName} for ${metadata.name}"
            )
        }

      handleZipFile(zipFile, workDirectory, context)
    }
  }

  fun handleZipFile(
    zipFile: Path,
    workDirectory: Path,
    context: IEPluginContext,
  ): List<IERecipeDraft> {
    val zipContent = workDirectory.resolve("extracted")

    ZipUtils.unzipDir(zipFile, zipContent)

    val recipeFolders = zipContent.listDirectoryEntries().first().listDirectoryEntries()

    val importer = IEPluginNextcloudCookbookImporter(languageDetectorService)

    return recipeFolders.mapNotNull { recipeFolder ->
      val imageFile = recipeFolder.resolve("full.jpg").takeIf { it.exists() }
      val ldJsonFile = recipeFolder.resolve("recipe.json")

      if (!ldJsonFile.exists()) return@mapNotNull null

      val ldJson = context.objectMapper.readValue(ldJsonFile.toFile(), LDJsonRecipe::class.java)

      importer.import(ldJson, imageFile)
    }
  }

  override fun exportSingle(input: IERecipe, workDirectory: Path, context: IEPluginContext): Path {
    throw FBadRequestException("Exporting single recipes is not supported by ${metadata.name}")
  }

  /**
   * Creates a .zip file containing the exported recipes.
   *
   * export.zip
   * - Recipe 1/
   * - - recipe.json
   * - - full.jpg
   * - Recipe 2/
   * - - recipe.json
   * - - full.jpg
   */
  override fun exportMultiple(
    inputs: List<IERecipe>,
    workDirectory: Path,
    context: IEPluginContext,
  ): Path {
    val zipContent = workDirectory.resolve("zipContent")

    val exporter = IEPluginLDNextcloudCookbookExporter()

    for (input in inputs) {
      runCatching {
        val ldJson = exporter.export(input)
        val image = input.files.firstOrNull()

        val recipeFolder = zipContent.resolve("${input.label} - (${input.id})").createDirectories()

        context.objectMapper.writeValue(recipeFolder.resolve("recipe.json").toFile(), ldJson)

        /**
         * Only copy the full-resolution image. There is no need to generate thumbnails, because the
         * Nextcloud Cookbook App already does that.
         */
        image?.copyTo(recipeFolder.resolve("full.jpg").toFile())
      }
    }

    val zipFile =
      workDirectory.resolve(
        "Export ${
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
                }.zip"
      )

    ZipUtils.zipFile(sourceDir = zipContent, zipFile = zipFile)

    return zipFile
  }
}
