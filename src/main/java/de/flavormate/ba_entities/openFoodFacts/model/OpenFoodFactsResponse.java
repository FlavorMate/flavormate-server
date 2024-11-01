package de.flavormate.ba_entities.openFoodFacts.model;

/**
 * Represents a response from the OpenFoodFacts API.
 *
 * @param product        The product information returned by the OpenFoodFacts API.
 * @param status         The status code of the response.
 * @param status_verbose A verbose description of the status.
 * @param code           The barcode of the product.
 */
public record OpenFoodFactsResponse(OpenFoodFactsProduct product, int status, String status_verbose,
									String code) {
}

