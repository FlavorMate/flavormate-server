package de.flavormate.aa_interfaces.modules;

import de.flavormate.ba_entities.backup.model.ImportResponse;
import de.flavormate.ba_entities.recipe.model.Recipe;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface BackupModule<T> {

	ImportResponse restore(Path workingDirectory);

	void backup(Path workingDir, Path zipPath, List<Recipe> recipes, String id) throws IOException;

}
