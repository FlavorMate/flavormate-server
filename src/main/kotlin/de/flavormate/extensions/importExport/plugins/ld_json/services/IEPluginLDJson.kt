/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ld_json.services

import com.fasterxml.jackson.module.kotlin.readValue
import de.flavormate.extensions.importExport.interfaces.IEPlugin
import de.flavormate.extensions.importExport.interfaces.IEPluginContext
import de.flavormate.extensions.importExport.interfaces.IEPluginMetadata
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraft
import de.flavormate.extensions.importExport.models.inputSource.FileInputSource
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import de.flavormate.extensions.importExport.models.inputSource.UrlInputSource
import de.flavormate.extensions.importExport.plugins.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.plugins.ld_json.services.importer.IEPluginLDJsonDownloader
import de.flavormate.extensions.importExport.plugins.ld_json.services.importer.IEPluginLDJsonMapper
import jakarta.enterprise.context.ApplicationScoped
import java.io.OutputStream

@ApplicationScoped
class IEPluginLDJson : IEPlugin {

  override val metadata =
    IEPluginMetadata(
      id = "ld_json",
      name = "LD-JSON Plugin",
      version = "1.0.0",
      author = "FlavorMate",
      description = "Import and Export JSON-LD structured recipe data",
      import = true,
      export = false, // Export not yet implemented
      supportedMimeTypes = listOf("application/ld+json", "application/json"),
      supportedExtensions = listOf("json", "jsonld"),
    )

  override fun import(input: IEInputSource, context: IEPluginContext): IERecipeDraft {
    val downloader = IEPluginLDJsonDownloader(context)

    val ldJsonRecipe =
      when (input) {
        is UrlInputSource -> downloader.download(input.name)
        is FileInputSource -> context.objectMapper.readValue<LDJsonRecipe>(input.file)
        else -> throw IllegalArgumentException("LD-JSON import only supports URLs and files")
      }

    val mapper = IEPluginLDJsonMapper(context)

    return mapper.map(ldJsonRecipe)
  }

  override fun export(draft: IERecipeDraft, output: OutputStream, context: IEPluginContext) {
    // Not implemented yet
    super.export(draft, output, context)
  }
}
