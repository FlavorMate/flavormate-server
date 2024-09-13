package de.flavormate.ba_entities.ingredient.model;

import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.utils.NumberUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "unit_id", referencedColumnName = "id")
	private Unit unit;

	@NotNull
	@Column(nullable = false, columnDefinition = "TEXT")
	private String label;

	@Override
	public String toString() {
		return String.format("%s %s %s", NumberUtils.isPositive(NumberUtils.isDoubleInt(amount)),
				unit.getLabel(), label);
	}

}
