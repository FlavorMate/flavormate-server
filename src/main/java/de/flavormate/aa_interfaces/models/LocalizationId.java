package de.flavormate.aa_interfaces.models;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class LocalizationId {
	private Long foreignId;
	private String language;
}
