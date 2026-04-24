/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.flavormate.services.importer

import de.flavormate.extensions.importExport.models.IEPluginContext
import de.flavormate.shared.services.DownloadService
import de.flavormate.utils.URLUtils
import java.nio.file.Path

class IEPluginFlavorMateDownloader(
  private val context: IEPluginContext,
  private val downloadService: DownloadService,
) {
  fun download(url: String): Path? {
    val cleanedUrl = URLUtils.cleanURL(url)

    return downloadService.downloadFile(cleanedUrl, context.maxImageSize)
  }
}
