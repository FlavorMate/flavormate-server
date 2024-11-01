package de.flavormate.ba_entities.openFoodFacts.model;

public record OpenFoodFactsResponse(OpenFoodFactsProduct product, int status, String status_verbose,
									String code) {
}
