package de.flavormate.aa_interfaces.modules;

import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.wrapper.ScrapeResponse;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface BackupModule<T> {

	List<ScrapeResponse> restore(List<T> data) throws Exception;

	void backup(Path workingDir, Path zipPath, List<Recipe> recipes, String id) throws IOException;

}
