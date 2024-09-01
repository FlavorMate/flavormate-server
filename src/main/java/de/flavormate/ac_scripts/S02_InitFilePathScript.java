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
	private URL ROOT;

	public S02_InitFilePathScript() {
		super("Initialize File Path");
	}

	@Override
	public void run() {
		log("Start file path initialization");
		log("File path: {}", ROOT.toString());

		try {
			Path root = Paths.get(ROOT.getPath());
			if (!Files.exists(root)) {
				Files.createDirectories(root);
				log("Path {} created", ROOT.toExternalForm());

			}
			log("Path already exists");
		} catch (Exception e) {
			warning("Could not initialite path!");
		}
	}
}
