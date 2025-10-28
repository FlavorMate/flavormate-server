/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.shared.enums

import de.flavormate.utils.MimeTypes

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
  W480(width = 480, height = 270, fileStem = "W480"),
  W640(width = 640, height = 360, fileStem = "W640"),
  W960(width = 960, height = 540, fileStem = "w960"),
  W1280(width = 1280, height = 720, fileStem = "W1280"),
  W1920(width = 1920, height = 1080, fileStem = "w1920"),
  Original(width = 4096, height = 4096, fileStem = "original");

  val resolution: String
    get() = "${width}x${height}"

  val fileName: String
    get() = "$fileStem${MimeTypes.WEBP_EXTENSION}"
}
