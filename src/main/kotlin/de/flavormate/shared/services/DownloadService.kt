/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.services

import de.flavormate.exceptions.FBadRequestException
import io.quarkus.info.BuildInfo
import io.quarkus.logging.Log
import io.quarkus.runtime.configuration.MemorySize
import jakarta.enterprise.context.ApplicationScoped
import java.net.HttpURLConnection
import java.nio.file.Path
import kotlin.io.path.createTempFile
import org.apache.hc.core5.net.URIBuilder

@ApplicationScoped
class DownloadService(private val buildInfo: BuildInfo) {

  private val version
    get() = buildInfo.version()

  fun downloadFile(uriBuilder: URIBuilder, maxFileSize: MemorySize): Path? {
    try {
      val uri = uriBuilder.build()

      // 1. Validate scheme
      if (uri.scheme != "https") {
        Log.info("Recipe download aborted for $uri: No https")
        return null
      }

      // 2. Validate host is not null
      if (uri.host.isNullOrBlank()) {
        Log.info("Recipe download aborted for $uri: Invalid host")
        return null
      }

      // 3. Set timeouts and size limits to prevent DoS
      val connection =
        (uri.toURL().openConnection() as HttpURLConnection).apply {
          connectTimeout = 5000 // 5 seconds
          readTimeout = 10000 // 10 seconds
          setRequestProperty("User-Agent", "FlavorMate/${version}")
        }

      // 4. Check content length before downloading
      val contentLength = connection.contentLengthLong
      if (contentLength > maxFileSize.asLongValue()) {
        Log.info("Recipe download aborted for $uri: File too large ($contentLength bytes)")
        connection.disconnect()
        return null
      }

      // 5. Read with size limit and write directly to temp file
      val tmpFile = createTempFile()

      connection.inputStream.use { input ->
        tmpFile.toFile().outputStream().use { output ->
          val buffer = ByteArray(8192)
          var totalRead = 0L

          var bytesRead = input.read(buffer)
          while (bytesRead != -1) {
            totalRead += bytesRead
            if (totalRead > maxFileSize.asLongValue()) {
              connection.disconnect()
              tmpFile.toFile().delete()
              throw FBadRequestException(message = "File exceeds maximum size limit")
            }
            output.write(buffer, 0, bytesRead)
            bytesRead = input.read(buffer)
          }
        }
      }

      connection.disconnect()

      return tmpFile
    } catch (e: Exception) {
      Log.error("Failed to download recipes $uriBuilder", e)
      return null
    }
  }
}
