/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.utils

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
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
   * Extracts a ZIP archive into the specified target directory.
   *
   * This method protects against zip-slip attacks by validating the canonical path of each
   * extracted entry before writing it to disk.
   *
   * @param zipFile the ZIP archive to extract
   * @param targetDir the directory where the archive should be extracted
   * @throws IOException if an I/O error occurs during extraction
   */
  @Throws(IOException::class)
  fun unzipDir(zipFile: Path, targetDir: Path) {
    Files.createDirectories(targetDir)

    ZipInputStream(Files.newInputStream(zipFile)).use { zipIn ->
      var entry: ZipEntry? = zipIn.nextEntry

      while (entry != null) {
        val targetPath = targetDir.resolve(entry.name).normalize()

        if (!targetPath.startsWith(targetDir.normalize())) {
          throw IOException("Blocked zip entry outside target directory: ${entry.name}")
        }

        if (entry.isDirectory) {
          Files.createDirectories(targetPath)
        } else {
          Files.createDirectories(targetPath.parent)
          Files.newOutputStream(targetPath).use { output -> zipIn.copyTo(output) }
        }

        zipIn.closeEntry()
        entry = zipIn.nextEntry
      }
    }
  }
}
