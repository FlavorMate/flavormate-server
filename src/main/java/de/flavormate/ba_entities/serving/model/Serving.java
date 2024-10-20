package de.flavormate.ba_entities.serving.model;


import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.utils.NumberUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "servings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Serving extends BaseEntity {

	@NotNull
	@Column(nullable = false)
	private Double amount;

	@NotNull
	@Column(nullable = false)
	private String label;

	@Override
	public String toString() {
		return NumberUtils.beautify(amount) + " " + label;
	}

	public String toString(Integer requestedAmount) {
		return requestedAmount + " " + label;
	}

}
