package de.flavormate.ba_entities.openFoodFacts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenFoodFactsNutriments(@JsonProperty("carbohydrates_100g") double carbohydrates,

									  @JsonProperty("energy-kcal_100g") double energy_kcal,

									  @JsonProperty("fat_100g") double fat,

									  @JsonProperty("fiber_100g") double fiber,

									  @JsonProperty("proteins_100g") double proteins,

									  @JsonProperty("salt_100g") double salt,

									  @JsonProperty("saturated-fat_100g") double saturatedFat,

									  @JsonProperty("sodium_100g") double sodium,

									  @JsonProperty("sugars_100g") double sugars) {
}
