/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.categoryGroup.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.flavormate.aa_interfaces.models.ManualBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "category_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class CategoryGroup extends ManualBaseEntity {

  @NotNull @Column(nullable = false)
  private String label;

  @NotNull @OneToMany(
      mappedBy = "categoryGroup",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @Builder.Default
  @JsonIgnoreProperties({"categoryGroup"})
  private List<CategoryGroupLocalization> localizations = new ArrayList<>();

  public CategoryGroup(CategoryGroup group, String label) {
    super(group.id, group.version, group.createdOn, group.lastModifiedOn);
    this.label = label;
    this.localizations = group.localizations;
  }
}
