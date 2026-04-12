/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.inputSource

import java.io.File
import java.net.URI

sealed interface IEInputSource {
  val name: String?
}

data class UrlInputSource(val uri: URI, override val name: String = uri.toString()) :
  IEInputSource {
  companion object {
    fun fromString(url: String) = UrlInputSource(URI.create(url))
  }
}

data class FileInputSource(val file: File, override val name: String = file.name) : IEInputSource

enum class IEImportType {
  FileImport,
  UrlImport;

  fun isImportSupported(inputSource: IEInputSource): Boolean =
    when (this) {
      FileImport -> inputSource is FileInputSource
      UrlImport -> inputSource is UrlInputSource
    }
}
