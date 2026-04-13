/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.nextcloud_cookbook.services.importer

import de.flavormate.extensions.importExport.models.IEPluginContext
import de.flavormate.shared.services.DownloadService
import de.flavormate.utils.URLUtils
import java.nio.file.Path

class IEPluginNextcloudCookbookDownloader(
  private val context: IEPluginContext,
  private val downloadService: DownloadService,
) {
  fun download(url: String): Path? {
    val cleanedUrl = URLUtils.cleanURL(url)

    if (cleanedUrl.pathSegments.last() != "download") {
      cleanedUrl.appendPath("download")
    }

    return downloadService.downloadFile(cleanedUrl, context.maxImageSize)
  }
}
