/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.utils

import de.flavormate.shared.enums.ImageSquareResolution
import de.flavormate.shared.enums.ImageWideResolution
import java.io.IOException
import java.nio.file.Path

object ImageUtils {

  fun generateSquareImage(inputFile: Path, outputDir: Path) {
    for (entry in ImageSquareResolution.entries) {
      val outputFile = outputDir.resolve(entry.fileName)

      when (entry) {
        ImageSquareResolution.Original ->
          scaleMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)

        else -> resizeMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)
      }
    }
  }

  fun generateWideImage(inputFile: Path, outputDir: Path) {
    for (entry in ImageWideResolution.entries) {
      val outputFile = outputDir.resolve(entry.fileName)

      when (entry) {
        ImageWideResolution.Original ->
          scaleMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)

        else -> resizeMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)
      }
    }
  }

  fun resizeMagick(input: Path, output: Path, dimensions: String): Boolean {
    val command =
      listOf(
        "magick",
        input.toString(),
        "-resize",
        "${dimensions}^",
        "-gravity",
        "center",
        "-extent",
        dimensions,
        "-blur",
        "0x12",
        "-fill",
        "black",
        "-colorize",
        "40%",
        "(",
        input.toString(),
        "-resize",
        dimensions,
        "-gravity",
        "center",
        ")",
        "-composite",
        "-quality",
        "80",
        "-strip",
        output.toString(),
      )
    val result =
      ProcessBuilder(command)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor()

    if (result != 0) throw IOException("Failed to resize image")

    return true
  }

  fun scaleMagick(input: Path, output: Path, dimensions: String): Boolean {
    val command =
      listOf(
        "magick",
        input.toString(),
        "-resize",
        "${dimensions}>",
        "-quality",
        "80",
        "-strip",
        output.toString(),
      )
    val result =
      ProcessBuilder(command)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor()

    if (result != 0) throw IOException("Failed to scale image")
    return true
  }
}
