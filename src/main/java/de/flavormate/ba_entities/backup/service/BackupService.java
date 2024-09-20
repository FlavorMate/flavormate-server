package de.flavormate.ba_entities.backup.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ba_entities.backup.model.BackupType;
import de.flavormate.ba_entities.backup.modules.LdJsonModule;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.ba_entities.recipe.wrapper.ScrapeResponse;
import de.flavormate.ba_entities.scrape.model.LD_JSON;
import de.flavormate.utils.JSONUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupService {

	private final RecipeRepository recipeRepository;

	private final LdJsonModule ldJsonModule;

	@Value("${flavorMate.files-backup}")
	private URL backupPath;

	public Path backupAll(BackupType type, String language) throws IOException {
		String id = UUID.randomUUID().toString();

		var workingDir = Path.of(backupPath.getPath(), id);
		var zipPath = Path.of(backupPath.getPath(), LocalDate.now() + "_" + type + ".zip");

		createWorkingDir(workingDir);

		var recipes = fetchRecipes(language);

		try {
			switch (type) {
				case FLAVORMATE:
					break;
				case LD_JSON:
					ldJsonModule.backup(workingDir, zipPath, recipes, id);
					break;
			}
		} catch (Exception e) {
			log.error("Exception occurred: {}", e.getMessage());
		}

		removeWorkingDir(workingDir);

		return zipPath;
	}

	public List<ScrapeResponse> restoreAll(BackupType type, List<JsonNode> data) throws Exception {
		try {
			return switch (type) {
				case FLAVORMATE -> List.of();
				case LD_JSON -> {
					var modifiedData = data.stream().map((d) -> JSONUtils.mapper.convertValue(d, LD_JSON.class)).toList();
					yield ldJsonModule.restore(modifiedData);
				}
			};
		} catch (Exception e) {
			log.error("Exception occurred: {}", e.getMessage());
			throw e;
		}
	}


	private void createWorkingDir(Path workingDir) throws IOException {
		if (!Files.exists(workingDir)) {
			Files.createDirectories(workingDir);
			log.info("Path {} created", workingDir);
		} else {
			log.info("Working dir {} already exists", workingDir);
		}
	}

	private void removeWorkingDir(Path workingDir) throws IOException {
		FileUtils.deleteDirectory(workingDir.toFile());
	}

	private List<Recipe> fetchRecipes(String language) {
		var recipes = recipeRepository.findAll();
		recipes.forEach((recipe) -> recipe.translate(language));
		return recipes;
	}
}
