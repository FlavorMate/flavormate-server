package de.flavormate.ba_entities.ingredient.wrapper;

import de.flavormate.ba_entities.unit.model.Unit;

public record IngredientDraft(Double amount, Unit unit, String label) {

}
