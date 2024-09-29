package de.flavormate.ba_entities.recipe.wrapper;

import de.flavormate.ba_entities.backup.model.flavorMate.FMBackup;

import java.util.List;

public record ScrapeResponse(RecipeDraft recipe, List<String> images) {

	public static ScrapeResponse from(FMBackup backup, List<String> images) {
		return new ScrapeResponse(backup.toRecipeDraft(), images);
	}
}
