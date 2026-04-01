/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.services

import de.flavormate.extensions.importExport.interfaces.IEPlugin
import de.flavormate.extensions.importExport.interfaces.IEPluginContext
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraft
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import java.io.OutputStream

@ApplicationScoped
class IEPluginManager(
  private val plugins: Instance<IEPlugin>,
  private val currentContextProvider: IEPluginContextProvider,
) {
  fun getAllPlugins(): List<IEPlugin> = plugins.toList()

  fun getImportPlugins(): List<IEPlugin> = plugins.filter { it.metadata.import }

  fun getExportPlugins(): List<IEPlugin> = plugins.filter { it.metadata.export }

  fun getPluginById(id: String): IEPlugin? = plugins.find { it.metadata.id == id }

  fun findImportPluginByMimeType(mimeType: String): IEPlugin? =
    plugins.find {
      it.metadata.import && it.metadata.supportedMimeTypes.contains(mimeType.lowercase())
    }

  fun findImportPluginByExtension(extension: String): IEPlugin? =
    plugins.find {
      it.metadata.import &&
        it.metadata.supportedExtensions.contains(extension.lowercase().removePrefix("."))
    }

  fun import(pluginId: String, input: IEInputSource): IERecipeDraft {
    val plugin =
      getPluginById(pluginId) ?: throw IllegalArgumentException("Plugin $pluginId not found")
    if (!plugin.metadata.import)
      throw IllegalArgumentException("Plugin $pluginId does not support import")

    val context = createContext()

    return plugin.import(input, context)
  }

  fun export(pluginId: String, draft: IERecipeDraft, output: OutputStream) {
    val plugin =
      getPluginById(pluginId) ?: throw IllegalArgumentException("Plugin $pluginId not found")
    if (!plugin.metadata.export)
      throw IllegalArgumentException("Plugin $pluginId does not support export")
    plugin.export(draft, output, createContext())
  }

  fun createContext() =
    IEPluginContext(
      currentUser = currentContextProvider.currentUser,
      objectMapper = currentContextProvider.objectMapper,
      maxImageSize = currentContextProvider.maxImageSize,
    )
}
