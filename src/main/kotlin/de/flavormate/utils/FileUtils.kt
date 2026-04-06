/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.utils

import jakarta.ws.rs.core.StreamingOutput
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.exists

object FileUtils {
  fun streamFile(file: Path): StreamingOutput {
    if (!file.exists()) throw IOException("File not found")

    return StreamingOutput { output: OutputStream? ->
      Files.newInputStream(file).use { `in` ->
        val buffer = ByteArray(8192) // 8KB buffer
        var bytesRead: Int
        while ((`in`.read(buffer).also { bytesRead = it }) != -1) {
          output!!.write(buffer, 0, bytesRead)
        }
        output!!.flush()
      }
    }
  }

  fun zip(sourceDir: Path, zipFile: Path) {
    Files.newOutputStream(zipFile).use { os ->
      ZipOutputStream(os).use { zipOut ->
        Files.walk(sourceDir).use { paths ->
          paths.forEach { path ->
            val relative = sourceDir.relativize(path).toString().replace('\\', '/')
            if (relative.isEmpty()) return@forEach

            val entryName = if (Files.isDirectory(path)) "$relative/" else relative
            val entry = ZipEntry(entryName)

            zipOut.putNextEntry(entry)

            if (Files.isRegularFile(path)) {
              Files.newInputStream(path).use { input -> input.copyTo(zipOut) }
            }

            zipOut.closeEntry()
          }
        }
      }
    }
  }
}
