package de.flavormate.ba_entities.nutrition.wrapper;

public record NutritionDraft(String openFoodFactsId,
                             double carbohydrates,
                             double energyKcal,
                             double fat,
                             double saturatedFat,
                             double sugars,
                             double fiber,
                             double proteins,
                             double salt,
                             double sodium) {
}
