package de.flavormate.ba_entities.backup.model;

import de.flavormate.ba_entities.recipe.wrapper.ScrapeResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ImportResponse {
	int failed = 0;
	List<ScrapeResponse> processed = new ArrayList<>();

	public void incrementFailed() {
		failed++;
	}

	public void addImportResponse(ImportResponse importResponse) {
		this.failed += importResponse.getFailed();
		processed.addAll(importResponse.getProcessed());
	}
}
