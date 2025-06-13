/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.instruction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.utils.NumberUtils;
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

  @NotNull @Column(nullable = false, columnDefinition = "TEXT")
  private String label;

  @JsonIgnore
  public String getCalculatedLabel(double factor) {
    var copy = label;
    int lIndex = -1;
    int rIndex = -1;
    do {
      lIndex = copy.indexOf("[[", lIndex + 1);
      rIndex = copy.indexOf("]]", rIndex + 1);

      if (lIndex != -1) {
        var foundText = copy.substring(lIndex + 2, rIndex);
        double newValue = NumberUtils.tryParseDouble(foundText, 1.0);
        newValue *= factor;
        copy = copy.replaceAll("\\[\\[" + foundText + "]]", NumberUtils.beautify(newValue));
      }
    } while (lIndex != -1);
    return copy;
  }
}
