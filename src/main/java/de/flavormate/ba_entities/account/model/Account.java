/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.account.model;

import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.file.model.File;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.ba_entities.role.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Account extends BaseEntity {

  @Column(nullable = false)
  @NotNull private String displayName;

  @Column(nullable = false)
  @NotNull private String username;

  private String mail;

  @Column(nullable = false)
  @NotNull private String password;

  @ManyToOne
  @JoinColumn(name = "avatar", referencedColumnName = "id")
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private File avatar;

  @Column(nullable = false)
  @NotNull @Builder.Default
  private Instant lastActivity = Instant.now();

  @Column(nullable = false)
  @NotNull @Builder.Default
  private Boolean valid = false;

  @OnDelete(action = OnDeleteAction.CASCADE)
  @ManyToMany
  @JoinTable(
      name = "account_roles",
      joinColumns = @JoinColumn(name = "account_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @NotNull @Builder.Default
  private List<Role> roles = new ArrayList<>();

  @Column(nullable = false)
  @NotNull @Builder.Default
  private RecipeDiet diet = RecipeDiet.Meat;

  public boolean hasRole(String label) {
    return roles.stream().anyMatch(r -> r.getLabel().equals(label));
  }
}
