/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.nextcloudCookbook.services

import de.flavormate.exceptions.FBadRequestException
import de.flavormate.exceptions.FInternalErrorException
import de.flavormate.extensions.importExport.interfaces.IEPlugin
import de.flavormate.extensions.importExport.models.IEPluginContext
import de.flavormate.extensions.importExport.models.IEPluginMetadata
import de.flavormate.extensions.importExport.models.ieRecipe.IERecipe
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraft
import de.flavormate.extensions.importExport.models.inputSource.FileInputSource
import de.flavormate.extensions.importExport.models.inputSource.IEImportType
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import de.flavormate.extensions.importExport.models.inputSource.UrlInputSource
import de.flavormate.extensions.importExport.plugins.ldJson.models.LDJsonRecipe
import de.flavormate.extensions.importExport.plugins.nextcloudCookbook.services.exporter.IEPluginLDNextcloudCookbookExporter
import de.flavormate.extensions.importExport.plugins.nextcloudCookbook.services.importer.IEPluginNextcloudCookbookDownloader
import de.flavormate.extensions.importExport.plugins.nextcloudCookbook.services.importer.IEPluginNextcloudCookbookImporter
import de.flavormate.shared.enums.Language
import de.flavormate.shared.services.DownloadService
import de.flavormate.shared.services.LanguageDetectorService
import de.flavormate.utils.ZipUtils
import jakarta.enterprise.context.ApplicationScoped
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.streams.asSequence

@ApplicationScoped
class IEPluginNextcloudCookbook(
  private val languageDetectorService: LanguageDetectorService,
  private val downloadService: DownloadService,
) : IEPlugin {
  override val metadata =
    IEPluginMetadata(
      id = "nextcloud_cookbook",
      name = mapOf(Language.EN to "Nextcloud Cookbook", Language.DE to "Nextcloud Kochbuch"),
      version = "1.0.0",
      author = "FlavorMate",
      import = listOf(IEImportType.FileImport, IEImportType.UrlImport),
      importMimeTypes = listOf("application/zip"),
      importExtensions = listOf("zip"),
      importShortDescription =
        mapOf(
          Language.EN to "Import Nextcloud Cookbook",
          Language.DE to "Nextcloud Kochbuch importieren",
        ),
      importLongDescription =
        mapOf(
          Language.EN to "Import recipes from Nextcloud Cookbook zip files or URLs.",
          Language.DE to "Rezepte aus Nextcloud Kochbuch-ZIP-Dateien oder URLs importieren.",
        ),
      export = true,
      exportShortDescription =
        mapOf(
          Language.EN to "Export Nextcloud Cookbook",
          Language.DE to "Nextcloud Kochbuch exportieren",
        ),
      exportLongDescription =
        mapOf(
          Language.EN to "Export recipes as Nextcloud Cookbook zip archive.",
          Language.DE to "Rezepte als Nextcloud Kochbuch-ZIP-Archiv exportieren.",
        ),
    )

  override fun import(
    inputs: List<IEInputSource>,
    workDirectory: Path,
    context: IEPluginContext,
  ): List<IERecipeDraft> {
    val downloader = IEPluginNextcloudCookbookDownloader(context, downloadService)

    return inputs.flatMap { input ->
      if (metadata.import.none { it.isImportSupported(input) }) {
        throw FBadRequestException(
          message = "Unsupported import type ${input::class.simpleName} for ${metadata.name}"
        )
      }
      val zipFile =
        when (input) {
          is FileInputSource -> {
            input.file.toPath()
          }

          is UrlInputSource -> {
            downloader.download(input.name)
              ?: throw FInternalErrorException("Download failed for ${input.name}")
          }
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

    val rootDirectory =
      Files.find(
          zipContent,
          3,
          { path, attrs ->
            attrs.isRegularFile && path.name.equals("recipe.json", ignoreCase = true)
          },
        )
        .use { stream -> stream.asSequence().firstOrNull()?.parent?.parent }
        ?: throw FBadRequestException("No recipes found in zip file")

    if (!rootDirectory.normalize().startsWith(zipContent.normalize())) {
      throw FBadRequestException("Invalid zip file structure: path traversal detected")
    }

    val recipeFolders = rootDirectory.listDirectoryEntries()

    val importer = IEPluginNextcloudCookbookImporter(languageDetectorService)

    return recipeFolders.mapNotNull { recipeFolder ->
      val imageFile = recipeFolder.resolve("full.jpg").takeIf { it.exists() }
      val ldJsonFile = recipeFolder.resolve("recipe.json")

      if (!ldJsonFile.exists()) return@mapNotNull null

      val ldJson = context.objectMapper.readValue(ldJsonFile.toFile(), LDJsonRecipe::class.java)

      importer.import(ldJson, imageFile)
    }
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
  override fun export(inputs: List<IERecipe>, workDirectory: Path, context: IEPluginContext): Path {
    val zipContent = workDirectory.resolve("zipContent")

    val exporter = IEPluginLDNextcloudCookbookExporter()

    for (input in inputs) {
      runCatching {
        val ldJson = exporter.export(input)
        val image = input.files.firstOrNull()

        val recipeFolder = zipContent.resolve("${input.label} - (${input.id})").createDirectories()

        context.objectMapper.writeValue(recipeFolder.resolve("recipe.json").toFile(), ldJson)

        /*
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
