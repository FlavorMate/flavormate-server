/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.helpers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SNutritionInformation(
    String calories,
    String carbohydrateContent,
    String cholesterolContent,
    String fatContent,
    String fiberContent,
    String proteinContent,
    String saturatedFatContent,
    String servingSize,
    String sodiumContent,
    String sugarContent,
    String transFatContent,
    String unsaturatedFatContent) {}
