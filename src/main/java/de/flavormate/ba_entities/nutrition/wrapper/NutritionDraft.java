/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.nutrition.wrapper;

public record NutritionDraft(
    String openFoodFactsId,
    Double carbohydrates,
    Double energyKcal,
    Double fat,
    Double saturatedFat,
    Double sugars,
    Double fiber,
    Double proteins,
    Double salt,
    Double sodium) {}
