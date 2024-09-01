package de.flavormate.ba_entities.role.model;


import de.flavormate.aa_interfaces.models.ManualBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Role extends ManualBaseEntity {

	@NotNull
	@Column(nullable = false)
	private String label;

}
