package de.flavormate.ba_entities.ingredient.wrapper;

import de.flavormate.ba_entities.nutrition.wrapper.NutritionDraft;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.ba_entities.unit.model.UnitLocalized;

public record IngredientDraft(Double amount,
                              Unit unit,
                              String label,
                              UnitLocalized unitLocalized,
                              NutritionDraft nutrition) {

}
