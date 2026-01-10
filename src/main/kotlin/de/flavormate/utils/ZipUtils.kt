/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/** Utility class that provides methods for compressing directories into ZIP files. */
object ZipUtils {
  /**
   * Compresses a file or directory into a ZIP archive.
   *
   * @param fileToZip the file or directory to be zipped
   * @param fileName the name of the file within the ZIP archive
   * @param zipOut the ZipOutputStream to write the compressed data
   * @throws IOException if an I/O error occurs
   */
  @Throws(IOException::class)
  private fun zipFile(fileToZip: File, fileName: String, zipOut: ZipOutputStream) {
    if (fileToZip.isHidden) {
      return
    }
    if (fileToZip.isDirectory) {
      if (fileName.endsWith("/")) {
        zipOut.putNextEntry(ZipEntry(fileName))
        zipOut.closeEntry()
      } else {
        zipOut.putNextEntry(ZipEntry("$fileName/"))
        zipOut.closeEntry()
      }
      val children = fileToZip.listFiles()
      for (childFile in children!!) {
        zipFile(childFile, fileName + "/" + childFile.name, zipOut)
      }
      return
    }
    val fis = FileInputStream(fileToZip)
    val zipEntry = ZipEntry(fileName)
    zipOut.putNextEntry(zipEntry)
    val bytes = ByteArray(1024)
    var length: Int
    while ((fis.read(bytes).also { length = it }) >= 0) {
      zipOut.write(bytes, 0, length)
    }
    fis.close()
  }

  /**
   * Compresses the specified directory into a ZIP file at the given output path.
   *
   * @param input the path to the directory to be zipped
   * @param output the path to the output ZIP file
   * @throws IOException if an I/O error occurs during zipping
   */
  @Throws(IOException::class)
  fun zipDir(input: Path, output: Path) {
    val fos = FileOutputStream(output.toString())
    val zipOut = ZipOutputStream(fos)

    zipFile(input.toFile(), input.toFile().name, zipOut)
    zipOut.close()
    fos.close()
  }
}
