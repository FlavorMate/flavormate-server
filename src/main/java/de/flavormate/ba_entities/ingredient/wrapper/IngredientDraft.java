package de.flavormate.ba_entities.ingredient.wrapper;

import de.flavormate.ba_entities.nutrition.wrapper.NutritionDraft;
import de.flavormate.ba_entities.unit.model.Unit;

public record IngredientDraft(Double amount, Unit unit, String label, NutritionDraft nutrition) {

}
