package de.flavormate.ba_entities.recipe.enums;

import java.util.List;

public enum RecipeDiet {
	Meat, Fish, Vegetarian, Vegan;


	public static List<RecipeDiet> getFilter(RecipeDiet diet) {
		return switch (diet) {
			case Meat -> List.of(RecipeDiet.Meat, RecipeDiet.Fish, RecipeDiet.Vegetarian,
					RecipeDiet.Vegan);
			case Fish -> List.of(RecipeDiet.Fish, RecipeDiet.Vegetarian, RecipeDiet.Vegan);
			case Vegetarian -> List.of(RecipeDiet.Vegetarian, RecipeDiet.Vegan);
			case Vegan -> List.of(RecipeDiet.Vegan);
		};
	}

	public static String[] getFilterNames(RecipeDiet diet) {
		return getFilter(diet).stream().map(d -> d.toString()).toArray(String[]::new);
	}
}
