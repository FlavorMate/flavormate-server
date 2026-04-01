/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models.inputSource

import java.io.File
import java.io.InputStream
import java.net.URI

sealed interface IEInputSource {
  val name: String?
}

data class StreamInputSource(val inputStream: InputStream, override val name: String? = null) :
  IEInputSource

data class UrlInputSource(val url: URI, override val name: String = url.toString()) : IEInputSource

data class FileInputSource(val file: File, override val name: String = file.name) : IEInputSource
