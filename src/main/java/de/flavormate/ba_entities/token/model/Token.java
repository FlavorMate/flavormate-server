/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.token.model;

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

  @NotNull @ManyToOne
  @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
  private Account owner;

  public static Token PasswordToken(Account owner) {
    return Token.builder()
        .type(TokenType.PASSWORD)
        .owner(owner)
        .validFor(Duration.ofHours(1))
        .build();
  }

  public boolean isValid() {
    return Instant.now().isBefore(createdOn.plus(validFor));
  }
}
