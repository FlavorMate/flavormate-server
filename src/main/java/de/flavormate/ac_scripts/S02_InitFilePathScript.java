package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.scripts.AScript;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class S02_InitFilePathScript extends AScript {

	@Value("${flavorMate.files}")
	private URL ROOT_FILES;

	@Value("${flavorMate.files-backup}")
	private URL ROOT_BACKUP;

	public S02_InitFilePathScript() {
		super("Initialize File Path");
	}

	@Override
	public void run() {
		log("Start file path initialization");
		log("File path: {}", ROOT_FILES.toString());

		try {
			Path root = Paths.get(ROOT_FILES.getPath());
			if (!Files.exists(root)) {
				Files.createDirectories(root);
				log("Path {} created", ROOT_FILES.toExternalForm());
			}
			log("File path already exists");
		} catch (Exception e) {
			warning("Could not initialize file path!");
		}

		try {
			Path root = Paths.get(ROOT_BACKUP.getPath());
			if (!Files.exists(root)) {
				Files.createDirectories(root);
				log("Path {} created", ROOT_BACKUP.toExternalForm());
			}
			log("Backup path already exists");
		} catch (Exception e) {
			warning("Could not initialize backup path!");
		}
	}
}
