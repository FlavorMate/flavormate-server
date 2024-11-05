/* Licensed under AGPLv3 2024 */
package de.flavormate.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/** Utility class that provides methods for compressing directories into ZIP files. */
public abstract class ZipUtils {

  /**
   * Compresses a file or directory into a ZIP archive.
   *
   * @param fileToZip the file or directory to be zipped
   * @param fileName the name of the file within the ZIP archive
   * @param zipOut the ZipOutputStream to write the compressed data
   * @throws IOException if an I/O error occurs
   */
  private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut)
      throws IOException {
    if (fileToZip.isHidden()) {
      return;
    }
    if (fileToZip.isDirectory()) {
      if (fileName.endsWith("/")) {
        zipOut.putNextEntry(new ZipEntry(fileName));
        zipOut.closeEntry();
      } else {
        zipOut.putNextEntry(new ZipEntry(fileName + "/"));
        zipOut.closeEntry();
      }
      File[] children = fileToZip.listFiles();
      for (File childFile : children) {
        zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
      }
      return;
    }
    FileInputStream fis = new FileInputStream(fileToZip);
    ZipEntry zipEntry = new ZipEntry(fileName);
    zipOut.putNextEntry(zipEntry);
    byte[] bytes = new byte[1024];
    int length;
    while ((length = fis.read(bytes)) >= 0) {
      zipOut.write(bytes, 0, length);
    }
    fis.close();
  }

  /**
   * Compresses the specified directory into a ZIP file at the given output path.
   *
   * @param input the path to the directory to be zipped
   * @param output the path to the output ZIP file
   * @throws IOException if an I/O error occurs during zipping
   */
  public static void zipDir(Path input, Path output) throws IOException {
    FileOutputStream fos = new FileOutputStream(output.toString());
    ZipOutputStream zipOut = new ZipOutputStream(fos);

    zipFile(input.toFile(), input.toFile().getName(), zipOut);
    zipOut.close();
    fos.close();
  }
}
