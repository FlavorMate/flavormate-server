/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.utils

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.copyTo
import kotlin.io.path.createDirectories
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream
import kotlin.use
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.apache.commons.compress.archivers.zip.ZipFile

/** Utility class that provides methods for compressing directories into ZIP files. */
object ZipUtils {

  /**
   * Compresses a directory into a ZIP file.
   *
   * @param sourceDir the path to the directory to be compressed
   * @param zipFile the path to the resulting ZIP file
   */
  fun zipFile(sourceDir: Path, zipFile: Path) {
    Files.newOutputStream(zipFile).use { os ->
      ZipArchiveOutputStream(os).use { zipOut ->
        Files.walk(sourceDir).use { paths ->
          paths.forEach { path ->
            val relative = sourceDir.relativize(path).toString().replace('\\', '/')
            if (relative.isEmpty()) return@forEach

            val entryName = if (Files.isDirectory(path)) "$relative/" else relative
            val entry = ZipArchiveEntry(path.toFile(), entryName)

            zipOut.putArchiveEntry(entry)

            if (Files.isRegularFile(path)) {
              path.inputStream().use { input -> input.copyTo(zipOut) }
            }

            zipOut.closeArchiveEntry()
          }
        }
      }
    }
  }

  /**
   * Extracts a ZIP archive into the specified target directory.
   *
   * This method protects against zip-slip attacks by validating the canonical path of each
   * extracted entry before writing it to disk.
   *
   * @param zipFile the ZIP archive to extract
   * @param targetDir the directory where the archive should be extracted
   * @throws IOException if an I/O error occurs during extraction
   */
  fun unzipDir(zipFile: Path, targetDir: Path) {
    targetDir.createDirectories()

    ZipFile.builder().setPath(zipFile).get().use { zip ->
      val entries = zip.entries
      while (entries.hasMoreElements()) {
        val entry = entries.nextElement() as ZipArchiveEntry
        val outPath = targetDir.resolve(entry.name).normalize()

        // Protect against Zip Slip
        require(outPath.startsWith(targetDir)) { "Bad zip entry: ${entry.name}" }

        if (entry.isDirectory) {
          outPath.createDirectories()
        } else {
          outPath.parent?.createDirectories()
          zip.getInputStream(entry).use { input ->
            outPath.outputStream().use { output -> input.copyTo(output) }
          }
        }
      }
    }
  }
}
