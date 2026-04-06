/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.services

import de.flavormate.exceptions.FBadRequestException
import de.flavormate.exceptions.FInternalErrorException
import de.flavormate.extensions.importExport.interfaces.IEPlugin
import de.flavormate.extensions.importExport.interfaces.IEPluginContext
import de.flavormate.extensions.importExport.models.ieRecipe.IERecipe
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraft
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import java.nio.file.Path

@ApplicationScoped
class IEPluginManager(
  private val plugins: Instance<IEPlugin>,
  private val currentContextProvider: IEPluginContextProvider,
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

  fun import(pluginId: String, input: List<IEInputSource>): List<IERecipeDraft> {
    val plugin =
      getPluginById(pluginId) ?: throw FInternalErrorException("Plugin $pluginId not found")

    if (plugin.metadata.import.isEmpty())
      throw FBadRequestException("Plugin $pluginId does not support import")

    val context = createContext()

    return plugin.import(input, context)
  }

  fun export(pluginId: String, workDirectory: Path, recipes: List<IERecipe>): Path {
    val plugin =
      getPluginById(pluginId) ?: throw FInternalErrorException("Plugin $pluginId not found")

    if (!plugin.metadata.export)
      throw FBadRequestException("Plugin $pluginId does not support export")

    return plugin.export(recipes, workDirectory, createContext())
  }

  fun createContext() =
    IEPluginContext(
      currentUser = currentContextProvider.currentUser,
      objectMapper = currentContextProvider.objectMapper,
      maxImageSize = currentContextProvider.maxImageSize,
    )
}
