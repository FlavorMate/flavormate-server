/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ld_json.services

import com.fasterxml.jackson.module.kotlin.readValue
import de.flavormate.exceptions.FBadRequestException
import de.flavormate.extensions.importExport.interfaces.IEPlugin
import de.flavormate.extensions.importExport.models.IEPluginContext
import de.flavormate.extensions.importExport.models.IEPluginMetadata
import de.flavormate.extensions.importExport.models.ieRecipe.IERecipe
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraft
import de.flavormate.extensions.importExport.models.inputSource.FileInputSource
import de.flavormate.extensions.importExport.models.inputSource.IEImportType
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import de.flavormate.extensions.importExport.models.inputSource.UrlInputSource
import de.flavormate.extensions.importExport.plugins.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.plugins.ld_json.services.exporter.IEPluginLDJsonExporter
import de.flavormate.extensions.importExport.plugins.ld_json.services.importer.IEPluginLDJsonDownloader
import de.flavormate.extensions.importExport.plugins.ld_json.services.importer.IEPluginLDJsonImporter
import de.flavormate.shared.enums.Language
import de.flavormate.shared.services.DownloadService
import de.flavormate.shared.services.LanguageDetectorService
import de.flavormate.utils.ZipUtils
import jakarta.enterprise.context.ApplicationScoped
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories

@ApplicationScoped
class IEPluginLDJson(
  private val languageDetectorService: LanguageDetectorService,
  private val downloadService: DownloadService,
) : IEPlugin {

  override val metadata =
    IEPluginMetadata(
      id = "ld_json",
      name = mapOf(Language.EN to "LD-JSON Plugin", Language.DE to "LD-JSON Plugin"),
      version = "1.0.0",
      author = "FlavorMate",
      import = listOf(IEImportType.FileImport, IEImportType.UrlImport),
      importMimeTypes = listOf("application/ld+json", "application/json"),
      importExtensions = listOf("json", "jsonld"),
      importShortDescription =
        mapOf(Language.EN to "Import LD-JSON", Language.DE to "LD-JSON importieren"),
      importLongDescription =
        mapOf(
          Language.EN to "Import recipe data from LD-JSON files or URLs.",
          Language.DE to "Rezeptdaten aus LD-JSON-Dateien oder URLs importieren.",
        ),
      export = true,
      exportShortDescription =
        mapOf(Language.EN to "Export LD-JSON", Language.DE to "LD-JSON exportieren"),
      exportLongDescription =
        mapOf(
          Language.EN to "Export recipes as LD-JSON archive.",
          Language.DE to "Rezepte als LD-JSON-Archiv exportieren.",
        ),
    )

  override fun import(
    inputs: List<IEInputSource>,
    workDirectory: Path,
    context: IEPluginContext,
  ): List<IERecipeDraft> {
    val downloader = IEPluginLDJsonDownloader(context)
    val mapper = IEPluginLDJsonImporter(context, languageDetectorService, downloadService)

    return inputs.map { input ->
      if (metadata.import.none { it.isImportSupported(input) }) {
        throw FBadRequestException(
          message = "Unsupported import type ${input::class.simpleName} for ${metadata.name}"
        )
      }

      val ldJson =
        when (input) {
          is UrlInputSource -> downloader.download(input.name)
          is FileInputSource -> context.objectMapper.readValue<LDJsonRecipe>(input.file)
        }

      mapper.import(ldJson)
    }
  }

  /**
   * Creates a .zip file containing the exported recipes.
   *
   * export.zip
   * - recipe1.json
   * - recipe2.json
   */
  override fun export(inputs: List<IERecipe>, workDirectory: Path, context: IEPluginContext): Path {
    val zipContent = workDirectory.resolve("zipContent").createDirectories()

    val exporter = IEPluginLDJsonExporter()

    for (input in inputs) {
      runCatching {
        val ldJson = exporter.export(input)

        val ldJsonFile = zipContent.resolve("${input.label} - (${input.id}).json")

        context.objectMapper
          .writerWithDefaultPrettyPrinter()
          .writeValue(ldJsonFile.toFile(), ldJson)
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
