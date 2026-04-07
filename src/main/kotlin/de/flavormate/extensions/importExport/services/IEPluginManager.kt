/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.services

import de.flavormate.exceptions.FBadRequestException
import de.flavormate.exceptions.FInternalErrorException
import de.flavormate.extensions.importExport.interfaces.IEPlugin
import de.flavormate.extensions.importExport.models.IEPluginContext
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.transaction.Transactional
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.copyTo
import org.apache.commons.io.FileUtils

@ApplicationScoped
class IEPluginManager(
  private val plugins: Instance<IEPlugin>,
  private val currentContextProvider: IEPluginContextProvider,
  private val recipeConvertService: IERecipeConvertService,
  private val recipeDraftConvertService: IERecipeDraftConvertService,
) {
  fun getAllPlugins(): List<IEPlugin> = plugins.toList()

  fun getImportPlugins(): List<IEPlugin> = plugins.filter { it.metadata.import.isNotEmpty() }

  fun getExportPlugins(): List<IEPlugin> = plugins.filter { it.metadata.export }

  fun getPluginById(id: String): IEPlugin? = plugins.find { it.metadata.id == id }

  fun findImportPluginByMimeType(mimeType: String): IEPlugin? =
    plugins.find {
      it.metadata.import.isNotEmpty() &&
        it.metadata.supportedMimeTypes.contains(mimeType.lowercase())
    }

  fun findImportPluginByExtension(extension: String): IEPlugin? =
    plugins.find {
      it.metadata.import.isNotEmpty() &&
        it.metadata.supportedExtensions.contains(extension.lowercase().removePrefix("."))
    }

  @Transactional
  fun importSingle(pluginId: String, input: IEInputSource): RecipeDraftEntity {
    val plugin = getImportPlugin(pluginId)

    val normalized = plugin.importSingle(input, createContext())

    return recipeDraftConvertService.convert(normalized)
  }

  @Transactional
  fun importMultiple(pluginId: String, input: List<IEInputSource>): List<RecipeDraftEntity> {
    val plugin = getImportPlugin(pluginId)

    val normalized = plugin.importMultiple(input, createContext())

    return normalized.map { recipeDraftConvertService.convert(it) }
  }

  fun exportSingle(pluginId: String, recipe: RecipeEntity): Path =
    export(pluginId) { plugin, workDirectory ->
      val normalized = recipeConvertService.convert(recipe)
      plugin.exportSingle(normalized, workDirectory, createContext())
    }

  fun exportMultiple(pluginId: String, recipes: List<RecipeEntity>): Path =
    export(pluginId) { plugin, workDirectory ->
      val normalized = recipes.map { recipeConvertService.convert(it) }
      plugin.exportMultiple(normalized, workDirectory, createContext())
    }

  private fun export(
    pluginId: String,
    callback: (plugin: IEPlugin, workDirectory: Path) -> Path,
  ): Path {
    val plugin =
      getPluginById(pluginId) ?: throw FInternalErrorException("Plugin $pluginId not found")

    if (!plugin.metadata.export)
      throw FBadRequestException("Plugin $pluginId does not support export")

    val workDirectory = Files.createTempDirectory("ie-export-single-$pluginId")
    val tmpFile = Files.createTempFile(null, null)

    try {
      val outputFile = callback(plugin, workDirectory)

      outputFile.copyTo(tmpFile)
      FileUtils.deleteDirectory(workDirectory.toFile())

      return tmpFile
    } catch (e: Exception) {
      FileUtils.deleteDirectory(workDirectory.toFile())
      Files.deleteIfExists(tmpFile)
      throw e
    }
  }

  private fun getImportPlugin(pluginId: String): IEPlugin {
    val plugin =
      getPluginById(pluginId) ?: throw FInternalErrorException("Plugin $pluginId not found")

    if (plugin.metadata.import.isEmpty())
      throw FBadRequestException("Plugin $pluginId does not support import")

    return plugin
  }

  private fun createContext() =
    IEPluginContext(
      currentUser = currentContextProvider.currentUser,
      objectMapper = currentContextProvider.objectMapper,
      maxImageSize = currentContextProvider.maxImageSize,
    )
}
