/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.openFoodFacts.model;

/**
 * Represents a product retrieved from the OpenFoodFacts API.
 *
 * @param nutriments The nutritional information of the product.
 */
public record OpenFoodFactsProduct(OpenFoodFactsNutriments nutriments) {}
