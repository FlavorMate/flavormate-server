/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.shared.services

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.shared.enums.FilePath
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.StreamingOutput
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import org.apache.commons.io.FileUtils

@ApplicationScoped
class FileService(private val flavorMateProperties: FlavorMateProperties) {

  private val path
    get() = flavorMateProperties.paths().files()

  fun rootPath(prefix: FilePath): Path {
    return Paths.get(path).resolve(prefix.path)
  }

  fun createPath(prefix: FilePath, uuid: String): Path {
    return readPath(prefix, uuid).also { it.createDirectories() }
  }

  fun readPath(prefix: FilePath, uuid: String): Path {
    return rootPath(prefix)
      .resolve(uuid.substring(0, 2))
      .resolve(uuid.substring(2, 4))
      .resolve(uuid)
  }

  fun streamFile(prefix: FilePath, uuid: String, fileName: String): StreamingOutput {
    val file = readPath(prefix, uuid).resolve(fileName)

    if (!file.exists()) throw FNotFoundException(message = "File not found")

    return de.flavormate.utils.FileUtils.streamFile(file)
  }

  fun deleteFolder(prefix: FilePath, uuid: String) {
    val folder = readPath(prefix, uuid)
    if (folder.exists()) {
      folder.toFile().deleteRecursively()
    }
  }

  fun copyFolder(oldPrefix: FilePath, oldUuid: String, newPrefix: FilePath, newUuid: String) {
    val oldPath = readPath(oldPrefix, oldUuid)
    val newPath = createPath(newPrefix, newUuid)

    FileUtils.copyDirectory(oldPath.toFile(), newPath.toFile())
  }
}
