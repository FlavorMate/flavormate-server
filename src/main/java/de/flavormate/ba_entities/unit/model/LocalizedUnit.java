package de.flavormate.ba_entities.unit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.flavormate.aa_interfaces.models.ManualBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "unit_localizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class LocalizedUnit extends ManualBaseEntity {

	@NotNull
	@ManyToOne
	@JoinColumn(name = "unit_ref", referencedColumnName = "id", nullable = false)
	private UnitRef unitRef;

	@NotNull
	@Column(nullable = false)
	private String language;

	@NotNull
	@Column(nullable = false)
	private String labelSg;
	private String labelSgAbrv;


	private String labelPl;
	private String labelPlAbrv;

	@JsonIgnore
	@Transient
	public String getLabel(double amount) {
		if (amount != 1.0) {
			if (!StringUtils.isBlank(labelPlAbrv)) {
				return labelPlAbrv;
			} else if (!StringUtils.isBlank(labelPl)) {
				return labelPl;
			}
		}
		if (!StringUtils.isBlank(labelSgAbrv)) {
			return labelSgAbrv;
		} else {
			return labelSg;
		}
	}
}
