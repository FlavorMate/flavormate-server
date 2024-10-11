package de.flavormate.ba_entities.unit.model;

import de.flavormate.aa_interfaces.models.ManualBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
}
