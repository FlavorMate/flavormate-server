/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.services

import de.flavormate.exceptions.FBadRequestException
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.importExport.models.IEPluginMetadata
import de.flavormate.extensions.importExport.models.inputSource.FileInputSource
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import de.flavormate.extensions.importExport.models.inputSource.UrlInputSource
import de.flavormate.features.recipe.repositories.RecipeRepository
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.StreamingOutput
import java.io.File
import kotlin.io.path.deleteIfExists
import kotlin.io.path.inputStream

@RequestScoped
class IEService(
  private val iePluginManager: IEPluginManager,
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

    val drafts = iePluginManager.import(pluginId, input = inputs)

    return drafts.map { it.id }
  }

  fun export(pluginId: String, recipeIds: List<String>): Response {
    if (recipeIds.isEmpty()) {
      throw FBadRequestException("No recipes provided")
    }

    val recipes = recipeIds.mapNotNull { recipeRepository.findById(it) }

    if (recipes.isEmpty()) {
      throw FNotFoundException(message = "Recipes not found")
    }

    val zipFile = iePluginManager.export(pluginId, recipes)

    val stream = StreamingOutput { output ->
      zipFile.inputStream().use { input -> input.copyTo(output) }
      zipFile.deleteIfExists()
    }

    return Response.ok(stream)
      .header("Content-Disposition", """attachment; filename="${zipFile.fileName}"""")
      .build()
  }
}
