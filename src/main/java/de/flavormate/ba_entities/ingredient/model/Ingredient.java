/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.ingredient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.nutrition.model.Nutrition;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.ba_entities.unit.model.UnitLocalized;
import de.flavormate.utils.NumberUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Ingredient extends BaseEntity {

  private Double amount;

  @ManyToOne(cascade = {CascadeType.PERSIST})
  @JoinColumn(name = "unit_id", referencedColumnName = "id")
  private Unit unit;

  @ManyToOne(cascade = {CascadeType.PERSIST})
  @JoinColumn(name = "nutrition_id", referencedColumnName = "id")
  private Nutrition nutrition;

  @ManyToOne
  @JoinColumn(name = "unit_localized", referencedColumnName = "id")
  private UnitLocalized unitLocalized;

  @NotNull @Column(nullable = false, columnDefinition = "TEXT")
  private String label;

  /**
   * Used to determine the schema of the ingredient <br>
   * 1: uses the old freetext unit -> must be migrated <br>
   * 2: uses the new unit system
   */
  @NotNull @JsonIgnore private int schema;

  @Override
  public String toString() {
    String amountLabel = null;

    if (amount != null) {
      amountLabel = NumberUtils.beautify(amount);
    }

    String unitLabel = null;

    if (unit != null) {
      unitLabel = unit.getLabel();
    } else if (unitLocalized != null) {
      unitLabel = unitLocalized.getLabel(amount);
    }

    return Stream.of(amountLabel, unitLabel, label)
        .filter(StringUtils::isNotEmpty)
        .collect(Collectors.joining(" "));
  }
}
