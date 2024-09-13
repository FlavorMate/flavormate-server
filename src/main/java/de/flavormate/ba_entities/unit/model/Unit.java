package de.flavormate.ba_entities.unit.model;

import de.flavormate.aa_interfaces.models.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a unit entity in the system. This class is annotated as an entity for Spring Data JPA
 * and is mapped to the "units" table.
 *
 * <p>
 * The class extends {@link BaseEntity}, which provides basic MongoDB-specific functionality.
 * </p>
 *
 * <p>
 * This class is annotated with Lombok's {@code @Data} annotation, which automatically generates
 * standard boilerplate code such as getters, setters, and the toString method.
 * </p>
 *
 * <p>
 * The {@code @NoArgsConstructor} and {@code @AllArgsConstructor} annotations are used to generate a
 * no-argument constructor and an all-argument constructor, respectively.
 * </p>
 *
 * <p>
 * The {@code @EqualsAndHashCode} and {@code @ToString} annotations from Lombok are used to generate
 * equals, hashCode, and toString methods based on the superclass and all fields.
 * </p>
 *
 * <p>
 * The {@code @Entity} annotation indicates that instances of this class are entities that are
 * managed by a JPA (Java Persistence API) entity manager.
 * </p>
 *
 * <p>
 * The {@code @Table} annotation specifies the details of the database table to which this entity is
 * mapped. In this case, it's mapped to the "units" table.
 * </p>
 */
@Entity
@Table(name = "units")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Unit extends BaseEntity {

	/**
	 * The label of the unit. This field is marked as non-nullable in the database.
	 */
	@NotNull
	@Column(nullable = false)
	private String label;
}
