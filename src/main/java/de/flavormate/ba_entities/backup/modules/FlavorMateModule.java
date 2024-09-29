package de.flavormate.ba_entities.backup.modules;

import de.flavormate.aa_interfaces.modules.BackupModule;
import de.flavormate.ba_entities.backup.model.ImportResponse;
import de.flavormate.ba_entities.backup.model.flavorMate.FMBackup;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.wrapper.ScrapeResponse;
import de.flavormate.utils.JSONUtils;
import de.flavormate.utils.ZipUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlavorMateModule implements BackupModule<FMBackup> {
	@Value("${flavorMate.files}")
	private URL filePath;

	@Override
	public ImportResponse restore(Path workingDirectory) {
		var response = new ImportResponse();

		var zipFiles = workingDirectory.toFile().listFiles();

		for (var zipFile : zipFiles) {
			try {
				var zipPath = Paths.get(workingDirectory.toString(), FilenameUtils.removeExtension(zipFile.getName()));
				Files.createDirectories(zipPath);
				ZipUtils.unzipFile(zipFile.toPath(), zipPath);
				FileUtils.forceDelete(zipFile);
			} catch (Exception e) {
				response.incrementFailed();
			}
		}

		var zipFolders = workingDirectory.toFile().listFiles();
		for (var zipFolder : zipFolders) {
			var recipesFolder = Paths.get(zipFolder.getPath(), "recipes");

			var recipes = recipesFolder.toFile().listFiles((dir, name) -> !name.equals("0_all_recipes.json"));
			for (var recipe : recipes) {
				try {
					var images = new ArrayList<String>();
					var draft = JSONUtils.mapper.readValue(recipe, FMBackup.class);

					if (draft.getFiles() != null)
						for (var image : draft.getFiles()) {
							var imagePath = Paths.get(zipFolder.getPath(), image);
							var bytes = Files.readAllBytes(imagePath);
							var base64 = Base64.getEncoder().encodeToString(bytes);
							images.add(base64);
						}

					response.getProcessed().add(ScrapeResponse.from(draft, images));

				} catch (Exception e) {
					response.incrementFailed();
				}
			}
		}
		return response;
	}

	@Override
	public void backup(Path workingDir, Path zipPath, List<Recipe> recipes, String id) throws IOException {

		Path recipesPath = Paths.get(workingDir.toString(), "recipes");
		Files.createDirectories(recipesPath);

		Path filesPath = Paths.get(workingDir.toString(), "data");
		Files.createDirectories(filesPath);

		var flavorMateBackups = new ArrayList<FMBackup>();

		for (var recipe : recipes) {
			var recipePath = Paths.get(recipesPath.toString(), recipe.getId() + ".json");

			var flavorMateBackup = FMBackup.fromRecipe(recipe);

			JSONUtils.mapper.writeValue(recipePath.toFile(), flavorMateBackup);
			flavorMateBackups.add(flavorMateBackup);

			if (!recipe.getFiles().isEmpty()) {
				var filePath = Paths.get(filesPath.toString(), recipe.getId() + "");
				Files.createDirectories(filePath);
				for (var file : recipe.getFiles()) {
					Files.copy(Paths.get(this.filePath.getPath(), file.getPath()), Paths.get(filePath.toString(), file.getId() + ".jpg"));
				}
			}
		}

		JSONUtils.mapper.writeValue(Paths.get(recipesPath.toString(), "0_all_recipes.json").toFile(), flavorMateBackups);

		ZipUtils.zipDir(workingDir, zipPath);
	}
}
