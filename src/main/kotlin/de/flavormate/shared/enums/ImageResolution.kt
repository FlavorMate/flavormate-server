/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.shared.enums

import de.flavormate.utils.MimeTypes
import java.nio.file.Path
import java.nio.file.Paths

enum class ImageSquareResolution(val width: Int, val height: Int, val fileStem: String) {
  P16(width = 16, height = 16, fileStem = "s16"),
  P32(width = 32, height = 32, fileStem = "s32"),
  P64(width = 64, height = 64, fileStem = "s64"),
  P128(width = 128, height = 128, fileStem = "s128"),
  P256(width = 256, height = 256, fileStem = "s256"),
  P512(width = 512, height = 512, fileStem = "s512"),
  P1024(width = 1024, height = 1024, fileStem = "s1024"),

  // defines the upper bounds
  Original(width = 4096, height = 4096, fileStem = "Original");

  val resolution: String
    get() = "${width}x${height}"

  val fileName: String
    get() = "$fileStem${MimeTypes.WEBP_EXTENSION}"
}

enum class ImageWideResolution(val width: Int, val height: Int, val fileStem: String) {
  W160(width = 160, height = 90, fileStem = "w160"),
  W256(width = 256, height = 144, fileStem = "w256"),
  W320(width = 320, height = 180, fileStem = "w320"),
  W480(width = 480, height = 270, fileStem = "w480"),
  W640(width = 640, height = 360, fileStem = "w640"),
  W960(width = 960, height = 540, fileStem = "w960"),
  W1280(width = 1280, height = 720, fileStem = "w1280"),
  W1920(width = 1920, height = 1080, fileStem = "w1920"),

  // defines the upper bounds
  Original(width = 4096, height = 4096, fileStem = "original");

  val resolution: String
    get() = "${width}x${height}"

  val fileName: String
    get() = "$fileStem${MimeTypes.WEBP_EXTENSION}"

  val path: Path
    get() = folder.resolve(fileName)

  companion object {
    val folder: Path = Paths.get("16_9")
  }
}

enum class ImageScaledResolution(val width: Int, val height: Int, val fileStem: String) {
  S128(width = 128, height = 128, fileStem = "s128"),
  S256(width = 256, height = 256, fileStem = "s256"),
  S512(width = 512, height = 512, fileStem = "s512"),
  S1024(width = 1024, height = 1024, fileStem = "s1024"),
  S2048(width = 2048, height = 2048, fileStem = "s2048"),
  S3072(width = 3072, height = 3072, fileStem = "s3072"),
  Original(width = 4096, height = 4096, fileStem = "original");

  val resolution: String
    get() = "${width}x${height}"

  val fileName: String
    get() = "$fileStem${MimeTypes.WEBP_EXTENSION}"

  val path: Path
    get() = folder.resolve(fileName)

  companion object {
    val folder: Path = Paths.get("scaled")
  }
}

enum class ImageOriginalResolution(val width: Int, val height: Int, val fileStem: String) {
  Original(width = 4096, height = 4096, fileStem = "original");

  val resolution: String
    get() = "${width}x${height}"

  val fileName: String
    get() = "$fileStem${MimeTypes.WEBP_EXTENSION}"

  val path: Path = Paths.get(fileName)
}
