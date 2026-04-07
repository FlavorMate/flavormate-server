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
import java.nio.file.Path
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

    val drafts = iePluginManager.importMultiple(pluginId, input = inputs)

    return drafts.map { it.id }
  }

  fun exportSingle(pluginId: String, recipeId: String): Response {
    val recipe =
      recipeRepository.findById(recipeId) ?: throw FNotFoundException(message = "Recipe not found")

    val file = iePluginManager.exportSingle(pluginId, recipe)

    return export(file)
  }

  fun exportMultiple(pluginId: String, recipeIds: List<String>): Response {
    if (recipeIds.isEmpty()) {
      throw FBadRequestException("No recipes provided")
    }

    val recipes = recipeIds.mapNotNull { recipeRepository.findById(it) }

    if (recipes.isEmpty()) {
      throw FNotFoundException(message = "Recipes not found")
    }

    val zipFile = iePluginManager.exportMultiple(pluginId, recipes)

    return export(zipFile)
  }

  private fun export(input: Path): Response {
    val stream = StreamingOutput { output ->
      input.inputStream().use { input -> input.copyTo(output) }
      input.deleteIfExists()
    }

    return Response.ok(stream)
      .header("Content-Disposition", """attachment; filename="${input.fileName}"""")
      .build()
  }
}
