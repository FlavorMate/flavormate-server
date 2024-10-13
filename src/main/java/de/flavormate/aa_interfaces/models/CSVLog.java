package de.flavormate.aa_interfaces.models;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVLog is a utility class for creating and writing CSV log files.
 * It maintains a list of rows representing the CSV content and provides
 * methods to add rows, write the CSV file to a specified path, and check if
 * the log contains any content.
 */
public class CSVLog {

	/**
	 * The name of the CSV log file, dynamically generated with a timestamp
	 * to ensure uniqueness.
	 */
	private final String name;

	/**
	 * Stores the rows of the CSV log. Each row is represented as a list of strings.
	 * The first entry is typically the header, followed by data rows.
	 */
	private final List<List<String>> rows;

	/**
	 * Number of fields in each row of the CSV log.
	 */
	private final int fields;

	/**
	 * Creates a new CSV log with the specified name and headers.
	 * The name is appended with the current timestamp to ensure uniqueness.
	 * The headers are added as the first row in the CSV log.
	 *
	 * @param name    the base name for the CSV log file
	 * @param headers the list of headers for the CSV log
	 */
	public CSVLog(String name, List<String> headers) {
		this.name = name + "_" + Instant.now() + ".csv";
		rows = new ArrayList<>();
		rows.add(headers);

		fields = headers.size();
	}

	/**
	 * Replaces all semicolons in the input string with underscores.
	 *
	 * @param s the input string to be cleaned
	 * @return a new string with all semicolons replaced by underscores
	 */
	private static String cleanString(String s) {
		return s.replaceAll(";", "_");
	}

	/**
	 * Adds a row of data to the CSV log.
	 *
	 * @param row the list of strings representing the row to be added.
	 *            The number of elements in the list must match the number
	 *            of fields specified in the header.
	 * @throws IllegalArgumentException if the number of elements in the row
	 *                                  does not match the number of fields.
	 */
	public void addRow(List<String> row) {
		if (row.size() != fields) {
			throw new IllegalArgumentException("Row has wrong number of fields");
		}

		rows.add(row);
	}

	/**
	 * Writes the CSV log content to a file at the specified migration path.
	 *
	 * @param migration the target directory where the CSV file will be written
	 * @return {@code true} if the file is successfully written; {@code false} otherwise
	 */
	public boolean writeFile(Path migration) {
		try {
			var file = Paths.get(migration.toString(), name).toFile();
			var contents = rows.stream().map(row -> String.join(";", row.stream().map(CSVLog::cleanString).toList())).toList();
			var content = String.join("\n", contents);
			FileUtils.writeStringToFile(file, content, "UTF-8");
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Checks if the CSV log contains any content beyond the header row.
	 *
	 * @return {@code true} if the log contains at least one data row; {@code false} otherwise
	 */
	public boolean hasContent() {
		return rows.size() > 1;
	}
}

