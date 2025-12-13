/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.utils

import de.flavormate.shared.enums.ImageOriginalResolution
import de.flavormate.shared.enums.ImageScaledResolution
import de.flavormate.shared.enums.ImageSquareResolution
import de.flavormate.shared.enums.ImageWideResolution
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.createDirectories

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

  /**
   * Creates multiple thumbnail images for the given images. The following images are created:
   * - Original => a scaled version of the original image if it's bigger than the max resolution
   * - 16/9 => a set of different resolutions with a blurred background if the original image is not
   *   16/9
   * - Scaled => a set of different resolutions keeping the original aspect ratio
   */
  fun createDynamicImage(inputFile: Path, outputDir: Path, newFile: Boolean = true) {
    outputDir.resolve(ImageWideResolution.folder).also { it.createDirectories() }
    outputDir.resolve(ImageScaledResolution.folder).also { it.createDirectories() }

    if (newFile) {
      // Create full image
      for (entry in ImageOriginalResolution.entries) {
        val outputFile = outputDir.resolve(entry.path)
        scaleMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)
      }
    }

    // Create 16/9 images
    for (entry in ImageWideResolution.entries) {
      val outputFile = outputDir.resolve(entry.path)

      when (entry) {
        ImageWideResolution.Original -> continue
        else -> resizeMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)
      }
    }

    // Create scaled images
    for (entry in ImageScaledResolution.entries) {
      val outputFile = outputDir.resolve(entry.path)

      when (entry) {
        ImageScaledResolution.Original -> continue
        else -> scaleMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)
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
