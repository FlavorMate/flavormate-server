/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ld_json.services

import com.fasterxml.jackson.module.kotlin.readValue
import de.flavormate.extensions.importExport.interfaces.IEPlugin
import de.flavormate.extensions.importExport.interfaces.IEPluginContext
import de.flavormate.extensions.importExport.interfaces.IEPluginMetadata
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
import de.flavormate.shared.services.LanguageDetectorService
import de.flavormate.utils.FileUtils
import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.copyTo
import kotlin.io.path.createParentDirectories

@ApplicationScoped
class IEPluginLDJson(private val languageDetectorService: LanguageDetectorService) : IEPlugin {

  override val metadata =
    IEPluginMetadata(
      id = "ld_json",
      name = "LD-JSON Plugin",
      version = "1.0.0",
      author = "FlavorMate",
      description = "Import and Export JSON-LD structured recipe data",
      import = listOf(IEImportType.FileImport, IEImportType.UrlImport),
      export = true, // Export not yet implemented
      supportedMimeTypes = listOf("application/ld+json", "application/json"),
      supportedExtensions = listOf("json", "jsonld"),
    )

  override fun import(inputs: List<IEInputSource>, context: IEPluginContext): List<IERecipeDraft> {
    val downloader = IEPluginLDJsonDownloader(context)
    val mapper = IEPluginLDJsonImporter(context, languageDetectorService)

    return inputs.mapNotNull { input ->
      if (metadata.import.none { it.isImportSupported(input) }) {
        Log.debug("Unsupported import type ${input::class.simpleName} for ${metadata.name}")
        return@mapNotNull null
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
   * Creates a zip file containing the exported recipes.
   *
   * export.zip
   * - recipe1.json
   * - recipe2.json
   */
  override fun export(inputs: List<IERecipe>, workDirectory: Path, context: IEPluginContext): Path {
    val zipContent = workDirectory.resolve("zipContent")

    val files =
      inputs.map { input ->
        val exporter = IEPluginLDJsonExporter(languageDetectorService)
        val outputFile = workDirectory.resolve("${input.label} (${input.id}).json")

        exporter.export(input)
        context.objectMapper.writeValue(outputFile.toFile(), exporter)

        outputFile
      }

    files.forEach { it.copyTo(zipContent.resolve(it.fileName).createParentDirectories()) }

    val zipFile =
      workDirectory.resolve(
        "Export ${
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
      }.zip"
      )

    FileUtils.zip(sourceDir = zipContent, zipFile = zipFile)

    return zipFile
  }
}
