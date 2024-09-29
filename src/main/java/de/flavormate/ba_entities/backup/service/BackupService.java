package de.flavormate.ba_entities.backup.service;

import de.flavormate.ba_entities.backup.model.BackupType;
import de.flavormate.ba_entities.backup.model.ImportResponse;
import de.flavormate.ba_entities.backup.modules.FlavorMateModule;
import de.flavormate.ba_entities.backup.modules.LdJsonModule;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	private final FlavorMateModule flavorMateModule;

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
					flavorMateModule.backup(workingDir, zipPath, recipes, id);
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

	public ImportResponse restoreAll(BackupType type, MultipartFile[] data) throws Exception {
		var response = new ImportResponse();
		String id = UUID.randomUUID().toString();

		var workingDir = Path.of(backupPath.getPath(), id);
		createWorkingDir(workingDir);

		for (var file : data) {
			var zipPath = Path.of(workingDir.toString(), UUID.randomUUID() + ".file");
			file.transferTo(zipPath);
		}


		try {
			switch (type) {
				case FLAVORMATE -> {
					response.addImportResponse(flavorMateModule.restore(workingDir));
				}
				case LD_JSON -> {
					response.addImportResponse(ldJsonModule.restore(workingDir));
				}
			}


//			return switch (type) {
//				case FLAVORMATE -> {
//					var modifiedData = data.stream().map((d) -> JSONUtils.mapper.convertValue(d, FMBackup.class)).toList();
//					yield flavorMateModule.restore(data);
//				}
//				case LD_JSON -> {
//					var modifiedData = data.stream().map((d) -> JSONUtils.mapper.convertValue(d, LD_JSON.class)).toList();
//					yield ldJsonModule.restore(modifiedData);
//				}
//			};
		} catch (Exception e) {
			log.error("Exception occurred: {}", e.getMessage());
			throw e;
		}

		removeWorkingDir(workingDir);

		return response;
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
