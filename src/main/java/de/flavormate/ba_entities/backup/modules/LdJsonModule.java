package de.flavormate.ba_entities.backup.modules;

import de.flavormate.aa_interfaces.modules.BackupModule;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.wrapper.ScrapeResponse;
import de.flavormate.ba_entities.scrape.model.LD_JSON;
import de.flavormate.ba_entities.scrape.service.ScrapeService;
import de.flavormate.utils.JSONUtils;
import de.flavormate.utils.ZipUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LdJsonModule implements BackupModule<LD_JSON> {

	private final ScrapeService scrapeService;

	@Value("${flavorMate.files}")
	private URL filePath;

	@Override
	public List<ScrapeResponse> restore(List<LD_JSON> data) throws Exception {
		return scrapeService.processScrapeResults(data);
	}

	@Override
	public void backup(Path workingDir, Path zipPath, List<Recipe> recipes, String id) throws IOException {

		Path recipesPath = Paths.get(workingDir.toString(), "recipes");
		Files.createDirectories(recipesPath);

		Path filesPath = Paths.get(workingDir.toString(), "data");
		Files.createDirectories(filesPath);

		var ldJsons = new ArrayList<LD_JSON>();

		for (var recipe : recipes) {
			var recipePath = Paths.get(recipesPath.toString(), recipe.getId() + ".json");
			var ld_json = LD_JSON.fromRecipe(recipe);
			ldJsons.add(ld_json);

			JSONUtils.mapper.writeValue(recipePath.toFile(), ld_json);

			if (!recipe.getFiles().isEmpty()) {
				var filePath = Paths.get(filesPath.toString(), recipe.getId() + "");
				Files.createDirectories(filePath);
				for (var file : recipe.getFiles()) {
					Files.copy(Paths.get(this.filePath.getPath(), file.getPath()), Paths.get(filePath.toString(), file.getId() + ".jpg"));
				}
			}
		}

		JSONUtils.mapper.writeValue(Paths.get(recipesPath.toString(), "0_all_recipes.json").toFile(), ldJsons);

		ZipUtils.zipDir(workingDir, zipPath);
	}
}
