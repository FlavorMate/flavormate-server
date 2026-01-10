/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.cron

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.enums.ImageResolution
import de.flavormate.shared.services.FileService
import de.flavormate.utils.ImageUtils
import de.flavormate.utils.MimeTypes
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.pathString
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

@ApplicationScoped
class OIDCCron(
  private val flavorMateProperties: FlavorMateProperties,
  private val fileService: FileService,
) {

  @Startup
  fun run() {
    Log.info("Start generating icons for OIDC providers")

    generateIcons()

    Log.info("Finished generating icons for OIDC providers.")
  }

  fun generateIcons() {
    val rootPath = fileService.rootPath(FilePath.OIDCProvider).createDirectories()

    for (item in rootPath.toFile().listFiles()) {
      FileUtils.forceDelete(item)
    }

    for (provider in flavorMateProperties.auth().oidc()) {
      if (provider.icon().isEmpty) continue

      val icon = Paths.get(provider.icon().get())

      val iconHasParent = icon.parent != null

      val sourcePath = Paths.get(flavorMateProperties.paths().providers()).resolve(icon)
      val targetPath =
        rootPath.resolve(FilenameUtils.removeExtension(icon.pathString) + MimeTypes.WEBP_EXTENSION)

      if (iconHasParent) {
        targetPath.parent.createDirectories()
      }

      if (!sourcePath.exists()) continue

      ImageUtils.scaleMagick(sourcePath, targetPath, ImageResolution.P256.resolution)
    }
  }
}
