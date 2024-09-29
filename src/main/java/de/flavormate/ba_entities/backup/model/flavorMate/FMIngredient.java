package de.flavormate.ba_entities.backup.model.flavorMate;

import de.flavormate.ba_entities.ingredient.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FMIngredient {
	private double amount;
	private String unit;
	private String label;


	public static FMIngredient fromIngredient(Ingredient ingredient) {
		String unit = null;

		if (ingredient.getUnit() != null)
			unit = ingredient.getUnit().getLabel();

		return new FMIngredient(ingredient.getAmount(), unit, ingredient.getLabel());
	}
}
