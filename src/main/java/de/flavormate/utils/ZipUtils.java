package de.flavormate.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Utility class that provides methods for compressing directories into ZIP files.
 */
public abstract class ZipUtils {

	/**
	 * Compresses a file or directory into a ZIP archive.
	 *
	 * @param fileToZip the file or directory to be zipped
	 * @param fileName  the name of the file within the ZIP archive
	 * @param zipOut    the ZipOutputStream to write the compressed data
	 * @throws IOException if an I/O error occurs
	 */
	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}
		if (fileToZip.isDirectory()) {
			// Only add directory entries if fileName is not empty
			if (!fileName.isEmpty()) {
				if (!fileName.endsWith("/")) {
					fileName += "/";
				}
				zipOut.putNextEntry(new ZipEntry(fileName));
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
	 * @param input  the path to the directory to be zipped
	 * @param output the path to the output ZIP file
	 * @throws IOException if an I/O error occurs during zipping
	 */
	public static void zipDir(Path input, Path output) throws IOException {
		FileOutputStream fos = new FileOutputStream(output.toString());
		ZipOutputStream zipOut = new ZipOutputStream(fos);

		zipFile(input.toFile(), "", zipOut);
		zipOut.close();
		fos.close();
	}

	public static void unzipFile(Path input, Path output) throws IOException {
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(input.toFile()));
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			File newFile = newFile(output.toFile(), zipEntry);
			if (zipEntry.isDirectory()) {
				if (!newFile.isDirectory() && !newFile.mkdirs()) {
					throw new IOException("Failed to create directory " + newFile);
				}
			} else {
				// fix for Windows-created archives
				File parent = newFile.getParentFile();
				if (!parent.isDirectory() && !parent.mkdirs()) {
					throw new IOException("Failed to create directory " + parent);
				}

				// write file content
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}
			zipEntry = zis.getNextEntry();
		}

		zis.closeEntry();
		zis.close();
	}

	private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}
}
