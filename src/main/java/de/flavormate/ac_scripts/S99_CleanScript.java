package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.scripts.AScript;
import de.flavormate.ad_configurations.flavormate.MiscConfig;
import de.flavormate.ad_configurations.flavormate.PathsConfig;
import de.flavormate.ba_entities.file.repository.FileRepository;
import de.flavormate.ba_entities.highlight.repository.HighlightRepository;
import de.flavormate.ba_entities.ingredient.repository.IngredientRepository;
import de.flavormate.ba_entities.tag.repository.TagRepository;
import de.flavormate.ba_entities.token.model.Token;
import de.flavormate.ba_entities.token.repository.TokenRepository;
import de.flavormate.ba_entities.unit.repository.UnitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class S99_CleanScript implements AScript {

	private final PathsConfig pathsConfig;

	private final MiscConfig miscConfig;


	private final FileRepository fileRepository;

	private final HighlightRepository highlightRepository;

	private final IngredientRepository ingredientRepository;

	private final TagRepository tagRepository;

	private final UnitRepository unitRepository;

	private final TokenRepository tokenRepository;


	public void run() throws Exception {
		log.info("Starting database cleaning");

		cleanContentFiles();
		cleanBackupFiles();
		cleanHighlights();
		cleanIngredients();
		cleanTags();
		cleanUnits();
		cleanTokens();

		log.info("Finished database cleaning");
	}

	private void cleanIngredients() {
		if (ingredientRepository.count() == 0) {
			log.info("Skipping ingredient cleaning");
			return;
		}

		var ingredients = ingredientRepository.findByEmptyUnit();
		log.info("Found {} unoptimized ingredients", ingredients.size());

		for (var ingredient : ingredients) {
			ingredient.setUnit(null);
			ingredientRepository.save(ingredient);
		}

		log.info("Optimized {} ingredients", ingredients.size());
	}

	private void cleanContentFiles() {
		if (fileRepository.count() == 0) {
			log.info("Skipping content file cleaning");
			return;
		}

		var files = fileRepository.findAll();

		for (var file : files) {
			var path = Paths.get(pathsConfig.content().getPath(), file.getPath());
			if (!Files.exists(path)) {
				log.info("File {} not found, deleting...", file.getPath());
				fileRepository.deleteById(file.getId());
			}
		}
	}

	private void cleanBackupFiles() throws Exception {

		if (fileRepository.count() == 0) {
			log.info("Skipping backup file cleaning");
			return;
		}

		try {
			var path = Paths.get(pathsConfig.backup().getPath());
			var files = path.toFile().listFiles();

			// if there are no files, delete all entries inside the database
			if (files == null) {
				fileRepository.deleteAll();
				return;
			}

			for (var file : files) {
				log.info("Deleting temporary backup path {}", file.getPath());
				FileUtils.forceDelete(file);
			}
		} catch (Exception e) {
			log.error("Backup files could not be cleaned");
			throw e;
		}
	}

	private void cleanHighlights() {
		if (highlightRepository.count() == 0) {
			log.info("Skipping highlight cleaning");
			return;
		}

		var offset = LocalDate.now().minusDays(miscConfig.highlightDays());
		var highlights = highlightRepository.findAllByDateBefore(offset);

		log.info("Found {} old highlights", highlights.size());

		if (highlights.isEmpty())
			return;

		log.info("Deleting old highlights");
		highlightRepository.deleteAll(highlights);

		log.info("Deleted {} highlights", highlights.size());
	}


	private void cleanTags() {
		if (tagRepository.count() == 0) {
			log.info("Skipping tag cleaning");
			return;
		}

		var tags = tagRepository.findAllEmpty();

		log.info("Found {} empty tags", tags.size());

		if (tags.isEmpty())
			return;

		log.info("Deleting empty tags");
		tagRepository.deleteAll(tags);

		log.info("Deleted {} tags", tags.size());
	}

	private void cleanUnits() {
		var units = unitRepository.findEmpty();

		if (units.isEmpty()) {
			log.info("Skipping unit cleaning");
			return;
		}

		log.info("Found {} invalid units", units.size());

		log.info("Deleting invalid units");

		unitRepository.deleteAll(units);

		log.info("Deleted {} units", units.size());
	}

	private void cleanTokens() {
		if (tokenRepository.count() == 0) {
			log.info("Skipping token cleaning");
			return;
		}

		var tokens = tokenRepository.findAll().stream().filter(Predicate.not(Token::isValid))
				.toList();


		log.info("Found {} invalid tokens", tokens.size());

		if (tokens.isEmpty())
			return;

		log.info("Deleting invalid tokens");
		tokenRepository.deleteAll(tokens);

		log.info("Deleted {} tokens", tokens.size());
	}
}
