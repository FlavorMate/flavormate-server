package de.flavormate.ba_entities.token.model;

import de.flavormate.aa_interfaces.models.BaseEntity;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.token.enums.TokenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
public class Token extends BaseEntity {

	@NotNull
	@Column(nullable = false)
	@Builder.Default
	private String token = UUID.randomUUID().toString();

	private Duration validFor;

	@Enumerated(value = EnumType.STRING)
	private TokenType type;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
	private Account owner;

	public static Token PasswordToken(Account owner) {
		return Token.builder().type(TokenType.PASSWORD).owner(owner).validFor(Duration.ofHours(1))
				.build();
	}

	public boolean isValid() {
		return createdOn.isBefore(createdOn.plus(validFor));
	}
}
