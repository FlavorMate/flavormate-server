/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.utils

import de.flavormate.shared.enums.ImageResolution
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.createDirectories

object ImageUtils {

  /**
   * Creates multiple thumbnail images for the given images. The following images are created:
   * - Original => a scaled version of the original image if it's bigger than the max resolution
   * - 16/9 => a set of different resolutions with a blurred background if the original image is not
   *   16/9
   * - Scaled => a set of different resolutions keeping the original aspect ratio
   */
  fun createDynamicImage(inputFile: Path, outputDir: Path, newFile: Boolean = true) {
    if (newFile) {
      // Create full image
      val entry = ImageResolution.Original
      val outputFile = outputDir.resolve(entry.path)
      outputFile.parent.createDirectories()
      scaleMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)
    }

    // Create 16/9 images
    for (entry in ImageResolution.wideResolutions) {
      val outputFile = outputDir.resolve(entry.path)
      outputFile.parent.createDirectories()
      resizeMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)
    }

    // Create scaled images
    for (entry in ImageResolution.scaledResolutions) {
      val outputFile = outputDir.resolve(entry.path)
      outputFile.parent.createDirectories()
      scaleMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)
    }
  }

  /**
   * Creates multiple thumbnail images for the given images. The following images are created:
   * - Original => a scaled version of the original image if it's bigger than the max resolution
   * - 1/1 => a set of different resolutions with a blurred background if the original image is not
   *   1/1
   */
  fun createPlaneImage(inputFile: Path, outputDir: Path, newFile: Boolean = true) {
    if (newFile) {
      // Create full image
      val entry = ImageResolution.Original
      val outputFile = outputDir.resolve(entry.path)
      outputFile.parent.createDirectories()
      scaleMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)
    }

    // Create 1:1 images
    for (entry in ImageResolution.planeResolutions) {
      val outputFile = outputDir.resolve(entry.path)
      outputFile.parent.createDirectories()
      resizeMagick(input = inputFile, output = outputFile, dimensions = entry.resolution)
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
