/* Licensed unter AGPLv3 2024 */
package de.flavormate.ba_entities.openFoodFacts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the nutritional information of a product as retrieved from the OpenFoodFacts API.
 *
 * @param carbohydrates The amount of carbohydrates per 100 grams.
 * @param energy_kcal The amount of energy in kilocalories per 100 grams.
 * @param fat The amount of fat per 100 grams.
 * @param fiber The amount of dietary fiber per 100 grams.
 * @param proteins The amount of proteins per 100 grams.
 * @param salt The amount of salt per 100 grams.
 * @param saturatedFat The amount of saturated fat per 100 grams.
 * @param sodium The amount of sodium per 100 grams.
 * @param sugars The amount of sugars per 100 grams.
 */
public record OpenFoodFactsNutriments(
    @JsonProperty("carbohydrates_100g") double carbohydrates,
    @JsonProperty("energy-kcal_100g") double energy_kcal,
    @JsonProperty("fat_100g") double fat,
    @JsonProperty("fiber_100g") double fiber,
    @JsonProperty("proteins_100g") double proteins,
    @JsonProperty("salt_100g") double salt,
    @JsonProperty("saturated-fat_100g") double saturatedFat,
    @JsonProperty("sodium_100g") double sodium,
    @JsonProperty("sugars_100g") double sugars) {}
