/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.token.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.token.enums.TokenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Token extends BaseEntity {

  @NotNull @Column(nullable = false)
  @Builder.Default
  private String token = UUID.randomUUID().toString();

  private Duration validFor;

  @Enumerated(value = EnumType.STRING)
  private TokenType type;

  @JsonIgnore
  @NotNull @ManyToOne
  @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
  private Account owner;

  private Long content;

  public static Token PasswordToken(Account owner) {
    return Token.builder()
        .type(TokenType.PASSWORD)
        .owner(owner)
        .validFor(Duration.ofHours(1))
        .build();
  }

  public static Token ShareToken(Account owner, long recipeId) {
    return Token.builder().type(TokenType.SHARE).owner(owner).content(recipeId).build();
  }

  public boolean isValid() {
    if (validFor == null) return true;

    return Instant.now().isBefore(createdOn.plus(validFor));
  }
}
