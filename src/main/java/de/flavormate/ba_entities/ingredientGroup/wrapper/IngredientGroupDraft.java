/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.ingredientGroup.wrapper;

import de.flavormate.ba_entities.ingredient.wrapper.IngredientDraft;
import java.util.List;

public record IngredientGroupDraft(String label, List<IngredientDraft> ingredients) {}
