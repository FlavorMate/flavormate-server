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
import kotlin.io.path.copyTo
import kotlin.io.path.createParentDirectories

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
      description =
        mapOf(
          Language.EN to "Import and Export JSON-LD structured recipe data",
          Language.DE to "Import und Export von JSON-LD-Strukturierten Rezeptdaten",
        ),
      import = listOf(IEImportType.FileImport, IEImportType.UrlImport),
      export = true, // Export not yet implemented
      supportedMimeTypes = listOf("application/ld+json", "application/json"),
      supportedExtensions = listOf("json", "jsonld"),
    )

  override fun importSingle(
    input: IEInputSource,
    workDirectory: Path,
    context: IEPluginContext,
  ): IERecipeDraft {
    val downloader = IEPluginLDJsonDownloader(context)
    val mapper = IEPluginLDJsonImporter(context, languageDetectorService, downloadService)

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

    return mapper.import(ldJson)
  }

  override fun importMultiple(
    inputs: List<IEInputSource>,
    workDirectory: Path,
    context: IEPluginContext,
  ): List<IERecipeDraft> {
    return inputs.mapNotNull {
      try {
        importSingle(it, workDirectory, context)
      } catch (_: Exception) {
        null
      }
    }
  }

  /** Creates a .json file containing the recipe */
  override fun exportSingle(input: IERecipe, workDirectory: Path, context: IEPluginContext): Path {
    val exporter = IEPluginLDJsonExporter()
    val outputFile = workDirectory.resolve("${input.label} (${input.id}).json")

    val ldJson = exporter.export(input)
    context.objectMapper.writeValue(outputFile.toFile(), ldJson)

    return outputFile
  }

  /**
   * Creates a .zip file containing the exported recipes.
   *
   * export.zip
   * - recipe1.json
   * - recipe2.json
   */
  override fun exportMultiple(
    inputs: List<IERecipe>,
    workDirectory: Path,
    context: IEPluginContext,
  ): Path {
    val zipContent = workDirectory.resolve("zipContent")

    val files = inputs.map { input -> exportSingle(input, workDirectory, context) }

    files.forEach { it.copyTo(zipContent.resolve(it.fileName).createParentDirectories()) }

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
