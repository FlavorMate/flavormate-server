/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.openFoodFacts.api.models

/**
 * Represents a product retrieved from the OpenFoodFacts API.
 *
 * @param nutriments The nutritional information of the product.
 */
data class OpenFoodFactsProduct(val nutriments: OpenFoodFactsNutriments)
