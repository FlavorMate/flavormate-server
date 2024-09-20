package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.scripts.AScript;
import de.flavormate.ba_entities.file.repository.FileRepository;
import de.flavormate.ba_entities.file.service.FileService;
import de.flavormate.ba_entities.highlight.repository.HighlightRepository;
import de.flavormate.ba_entities.tag.repository.TagRepository;
import de.flavormate.ba_entities.token.model.Token;
import de.flavormate.ba_entities.token.repository.TokenRepository;
import de.flavormate.ba_entities.unit.repository.UnitRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.function.Predicate;

@Service
@Transactional
public class S99_CleanScript extends AScript {

	@Value("${flavorMate.files}")
	URI filePath;
	@Value("${flavorMate.files-backup}")
	URI backupPath;

	@Value("${flavorMate.highlight.days}")
	private int HIGHLIGHT_DAYS;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private FileService fileService;
	@Autowired
	private HighlightRepository highlightRepository;
	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private UnitRepository unitRepository;
	@Autowired
	private TokenRepository tokenRepository;

	public S99_CleanScript() {
		super("Clean Service");
	}

	public void run() {
		log("Starting database cleaning");

		cleanFiles();
		cleanBackupFiles();
		cleanHighlights();
		cleanTags();
		cleanUnits();
		cleanTokens();

		log("Finished database cleaning");
	}

	private Boolean cleanFiles() {
		if (fileRepository.count() == 0) {
			log("Skipping file cleaning");
			return true;
		}

		try {
			var files = fileRepository.findAll();

			for (var file : files) {
				var path = Paths.get(filePath.getPath(), file.getPath());
				if (!Files.exists(path)) {
					log("File {} not found, deleting...", file.getPath());
					fileRepository.deleteById(file.getId());
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Boolean cleanBackupFiles() {

		if (fileRepository.count() == 0) {
			log("Skipping file cleaning");
			return true;
		}

		try {
			var path = Paths.get(backupPath.getPath());
			var files = path.toFile().listFiles();

			for (var file : files) {
				log("Deleting temporary backup path {}", file.getPath());
				FileUtils.forceDelete(file);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Boolean cleanHighlights() {
		try {
			if (highlightRepository.count() == 0) {
				log("Skipping highlight cleaning");
				return true;
			}

			var offset = LocalDate.now().minusDays(HIGHLIGHT_DAYS);
			var highlights = highlightRepository.findAllByDateBefore(offset);

			log("Found {} old highlights", highlights.size());

			if (highlights.isEmpty())
				return true;

			log("Deleting old highlights");
			highlightRepository.deleteAll(highlights);

			log("Deleted {} highlights", highlights.size());
			return true;
		} catch (Exception e) {
			warning("Highlights could not be cleaned");
			return false;
		}
	}


	private Boolean cleanTags() {
		try {
			if (tagRepository.count() == 0) {
				log("Skipping tag cleaning");
				return true;
			}

			var tags = tagRepository.findAllEmpty();

			log("Found {} empty tags", tags.size());

			if (tags.isEmpty())
				return true;

			log("Deleting empty tags");
			tagRepository.deleteAll(tags);

			log("Deleted {} tags", tags.size());
			return true;
		} catch (Exception e) {
			warning("Tags could not be cleaned");
			return false;
		}
	}

	private boolean cleanUnits() {
		try {
			var units = unitRepository.findEmpty();

			if (units.isEmpty()) {
				log("Skipping unit cleaning");
				return true;
			}

			log("Found {} invalid units", units.size());

			log("Deleting invalid units");

			unitRepository.deleteAll(units);

			log("Deleted {} units", units.size());
			return true;
		} catch (Exception e) {
			warning("Units could not be cleaned");
			return false;
		}
	}

	private Boolean cleanTokens() {
		try {
			if (tokenRepository.count() == 0) {
				log("Skipping token cleaning");
				return true;
			}

			var tokens = tokenRepository.findAll().stream().filter(Predicate.not(Token::isValid))
					.toList();


			log("Found {} invalid tokens", tokens.size());

			if (tokens.isEmpty())
				return true;

			log("Deleting invalid tokens");
			tokenRepository.deleteAll(tokens);

			log("Deleted {} tokens", tokens.size());
			return true;
		} catch (Exception e) {
			warning("Tokens could not be cleaned");
			return false;
		}
	}


}
