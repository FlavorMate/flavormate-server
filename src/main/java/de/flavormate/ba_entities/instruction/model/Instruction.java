package de.flavormate.ba_entities.instruction.model;


import de.flavormate.aa_interfaces.models.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "instructions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Instruction extends BaseEntity {

	@NotNull
	@Column(nullable = false, columnDefinition = "TEXT")
	private String label;

}
