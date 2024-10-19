package de.flavormate.ba_entities.ingredient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.nutrition.model.Nutrition;
import de.flavormate.ba_entities.unit.model.LocalizedUnit;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.utils.NumberUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Ingredient extends BaseEntity {

	@NotNull
	@Column(nullable = false)
	private Double amount;

	@ManyToOne(cascade = {CascadeType.PERSIST})
	@JoinColumn(name = "unit_id", referencedColumnName = "id")
	private Unit unit;

	@ManyToOne(cascade = {CascadeType.PERSIST})
	@JoinColumn(name = "nutrition_id", referencedColumnName = "id")
	private Nutrition nutrition;

	@ManyToOne
	@JoinColumn(name = "unit_localized", referencedColumnName = "id")
	private LocalizedUnit unitLocalized;

	@NotNull
	@Column(nullable = false, columnDefinition = "TEXT")
	private String label;

	/**
	 * Used to determine the schema of the ingredient <br>
	 * 1: uses the old freetext unit -> must be migrated <br>
	 * 2: uses the new unit system
	 */
	@NotNull
	@JsonIgnore
	private int schema;

	@Override
	public String toString() {
		// TODO: handle unit v2
		return String.format("%s %s %s", NumberUtils.isPositive(NumberUtils.isDoubleInt(amount)),
				Optional.ofNullable(unit).map(Unit::getLabel).orElse(""), label);
	}

}
