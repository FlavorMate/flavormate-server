/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.categoryGroup.model;

import de.flavormate.aa_interfaces.models.Localization;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "category_groups_i18n")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class CategoryGroupLocalization extends Localization {

  @ManyToOne
  @MapsId("foreignId")
  private CategoryGroup categoryGroup;
}
