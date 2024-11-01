/* Licensed under AGPLv3 2024 */
package de.flavormate.aa_interfaces.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public abstract class Localization {
  @EmbeddedId @EqualsAndHashCode.Include @ToString.Include private LocalizationId id;

  @EqualsAndHashCode.Include @ToString.Include private String value;
}
