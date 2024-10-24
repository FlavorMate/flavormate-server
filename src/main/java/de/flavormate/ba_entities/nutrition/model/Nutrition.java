package de.flavormate.ba_entities.nutrition.model;

import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.nutrition.wrapper.NutritionDraft;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "nutrition")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Nutrition extends BaseEntity {
	private String openFoodFactsId;

	// Nutritional values per 100g
	private double carbohydrates;
	private double energyKcal;
	private double fat;
	private double saturatedFat;
	private double sugars;
	private double fiber;
	private double proteins;
	private double salt;
	private double sodium;

	public static Nutrition fromNutritionDraft(NutritionDraft draft) {
		if (draft == null) return null;
		
		return Nutrition.builder()
				.openFoodFactsId(draft.openFoodFactsId())
				.carbohydrates(draft.carbohydrates())
				.energyKcal(draft.energyKcal())
				.fat(draft.fat())
				.saturatedFat(draft.saturatedFat())
				.sugars(draft.sugars())
				.fiber(draft.fiber())
				.proteins(draft.proteins())
				.salt(draft.salt())
				.sodium(draft.sodium())
				.build();
	}
}
