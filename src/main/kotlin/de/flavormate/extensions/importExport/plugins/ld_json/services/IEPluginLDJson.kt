/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ld_json.services

import com.fasterxml.jackson.module.kotlin.readValue
import de.flavormate.exceptions.FBadRequestException
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
import de.flavormate.shared.enums.Language
import jakarta.enterprise.context.ApplicationScoped
import java.nio.file.Path

@ApplicationScoped
class IEPluginLDJson : IEPlugin {

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

  override fun import(input: IEInputSource, context: IEPluginContext): IERecipeDraft {
    if (metadata.import.none { it.isImportSupported(input) }) {
      throw FBadRequestException(
        message = "Unsupported import type ${input::class.simpleName} for ${metadata.name}"
      )
    }

    val downloader = IEPluginLDJsonDownloader(context)

    val ldJsonRecipe =
      when (input) {
        is UrlInputSource -> downloader.download(input.name)
        is FileInputSource -> context.objectMapper.readValue<LDJsonRecipe>(input.file)
      }

    val mapper = IEPluginLDJsonImporter(context)

    return mapper.import(ldJsonRecipe)
  }

  override fun export(input: IERecipe, workDirectory: Path, context: IEPluginContext): Path {
    val exporter = IEPluginLDJsonExporter.export(input, language = Language.EN)

    val outputFile = workDirectory.resolve(input.label + ".json")

    context.objectMapper.writeValue(outputFile.toFile(), exporter)

    return outputFile
  }
}
