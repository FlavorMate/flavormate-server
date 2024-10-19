package de.flavormate.ba_entities.nutrition.model;

import de.flavormate.aa_interfaces.models.BaseEntity;
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
}
