/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.unit.model;

import de.flavormate.aa_interfaces.models.ManualBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "unit_refs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class UnitRef extends ManualBaseEntity {

  @NotNull private String description;
}
