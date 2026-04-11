/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.utils

import jakarta.ws.rs.core.StreamingOutput
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
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
}
