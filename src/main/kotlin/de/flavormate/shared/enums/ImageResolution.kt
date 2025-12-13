/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.shared.enums

import de.flavormate.utils.MimeTypes
import java.nio.file.Path
import java.nio.file.Paths

enum class ImageResolution(
  private val width: Int,
  private val height: Int,
  private val folder: String?,
  private val fileStem: String,
) {
  // defines the upper bounds
  Original(width = 4096, height = 4096, folder = null, fileStem = "original"),

  // 1:1 - Square / Plane
  P16(width = 16, height = 16, folder = "1_1", fileStem = "p16"),
  P32(width = 32, height = 32, folder = "1_1", fileStem = "p32"),
  P64(width = 64, height = 64, folder = "1_1", fileStem = "p64"),
  P128(width = 128, height = 128, folder = "1_1", fileStem = "p128"),
  P256(width = 256, height = 256, folder = "1_1", fileStem = "p256"),
  P512(width = 512, height = 512, folder = "1_1", fileStem = "p512"),
  P1024(width = 1024, height = 1024, folder = "1_1", fileStem = "p1024"),
  P2048(width = 2048, height = 2048, folder = "1_1", fileStem = "p2048"),
  P3072(width = 3072, height = 3072, folder = "1_1", fileStem = "p3072"),

  // 16:9 - Wide
  W160(width = 160, height = 90, folder = "16_9", fileStem = "w160"),
  W256(width = 256, height = 144, folder = "16_9", fileStem = "w256"),
  W320(width = 320, height = 180, folder = "16_9", fileStem = "w320"),
  W480(width = 480, height = 270, folder = "16_9", fileStem = "w480"),
  W640(width = 640, height = 360, folder = "16_9", fileStem = "w640"),
  W960(width = 960, height = 540, folder = "16_9", fileStem = "w960"),
  W1280(width = 1280, height = 720, folder = "16_9", fileStem = "w1280"),
  W1920(width = 1920, height = 1080, folder = "16_9", fileStem = "w1920"),
  W2560(width = 2560, height = 1440, folder = "16_9", fileStem = "w2560"),
  W3840(width = 3840, height = 2160, folder = "16_9", fileStem = "w3840"),

  // Original - Scaled
  S32(width = 32, height = 32, folder = "scaled", fileStem = "s32"),
  S64(width = 64, height = 64, folder = "scaled", fileStem = "s64"),
  S128(width = 128, height = 128, folder = "scaled", fileStem = "s128"),
  S256(width = 256, height = 256, folder = "scaled", fileStem = "s256"),
  S512(width = 512, height = 512, folder = "scaled", fileStem = "s512"),
  S1024(width = 1024, height = 1024, folder = "scaled", fileStem = "s1024"),
  S2048(width = 2048, height = 2048, folder = "scaled", fileStem = "s2048"),
  S3072(width = 3072, height = 3072, folder = "scaled", fileStem = "s3072");

  companion object {
    val planeResolutions = ImageResolution.entries.filter { it.name.startsWith("P") }.toList()
    val scaledResolutions = ImageResolution.entries.filter { it.name.startsWith("S") }.toList()
    val wideResolutions = ImageResolution.entries.filter { it.name.startsWith("W") }.toList()
  }

  val resolution: String
    get() = "${width}x${height}"

  val fileName: Path
    get() = Paths.get("$fileStem${MimeTypes.WEBP_EXTENSION}")

  val path: Path
    get() = folder?.let { Paths.get(it).resolve(fileName) } ?: fileName
}
