/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.services

import de.flavormate.exceptions.FBadRequestException
import de.flavormate.extensions.importExport.models.IEPluginMetadata
import de.flavormate.extensions.importExport.models.inputSource.FileInputSource
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import de.flavormate.extensions.importExport.models.inputSource.UrlInputSource
import de.flavormate.features.recipe.repositories.RecipeRepository
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.StreamingOutput
import java.io.File
import java.nio.file.Files
import org.apache.commons.io.FileUtils

@RequestScoped
class IEService(
  private val iePluginManager: IEPluginManager,
  private val ieRecipeDraftConvertService: IERecipeDraftConvertService,
  private val ieRecipeConvertService: IERecipeConvertService,
  private val recipeRepository: RecipeRepository,
) {

  fun getAvailableImporters(): List<IEPluginMetadata> {
    val importers = iePluginManager.getImportPlugins()

    return importers.map { it.metadata }
  }

  fun getAvailableExporters(): List<IEPluginMetadata> {
    val exporters = iePluginManager.getExportPlugins()

    return exporters.map { it.metadata }
  }

  fun import(pluginId: String, files: List<File>?, urls: List<String>?): List<String> {
    val inputs = mutableListOf<IEInputSource>()

    files?.forEach { inputs.add(FileInputSource(file = it)) }

    urls?.forEach { inputs.add(UrlInputSource.fromString(it)) }

    if (inputs.isEmpty()) throw FBadRequestException("No file or url provided")

    val drafts = iePluginManager.importMultiple(pluginId, input = inputs)

    return drafts.map { it.id }
  }

  fun export(pluginId: String, recipes: List<String>): Response {
    if (recipes.isEmpty()) {
      throw FBadRequestException("No recipes provided")
    }

    val workDirectory = Files.createDirectories(Files.createTempDirectory("ie-export-"))

    val files = recipes.mapNotNull { recipeRepository.findById(it) }

    val zipFile = iePluginManager.export(pluginId, workDirectory, files)

    val stream = StreamingOutput { output ->
      Files.newInputStream(zipFile).use { input -> input.copyTo(output) }
      FileUtils.deleteDirectory(workDirectory.toFile())
    }

    return Response.ok(stream)
      .header("Content-Disposition", """attachment; filename="${zipFile.fileName}"""")
      .build()
  }
}
