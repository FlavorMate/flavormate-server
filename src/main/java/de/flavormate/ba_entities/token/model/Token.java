/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.token.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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

  @JsonIncludeProperties({"displayName", "username"})
  @NotNull @ManyToOne
  @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
  private Account owner;

  private Long content;

  private Long uses;

  public static Token PasswordToken(Account owner) {
    return Token.builder()
        .type(TokenType.PASSWORD)
        .owner(owner)
        .validFor(Duration.ofHours(1))
        .uses(0L)
        .build();
  }

  public static Token ShareToken(Account owner, long recipeId, Duration duration) {
    return Token.builder()
        .type(TokenType.SHARE)
        .owner(owner)
        .content(recipeId)
        .validFor(duration)
        .uses(0L)
        .build();
  }

  public boolean isValid() {
    if (validFor == null) return true;

    return Instant.now().isBefore(createdOn.plus(validFor));
  }

  public void increaseUses() {
    uses++;
  }
}
