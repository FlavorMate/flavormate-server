/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.unit.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "unit_conversions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UnitConversion {
  @EmbeddedId private UnitConversionId id;

  private double factor;
}
