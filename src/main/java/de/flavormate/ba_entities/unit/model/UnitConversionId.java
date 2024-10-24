package de.flavormate.ba_entities.unit.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@EqualsAndHashCode
public class UnitConversionId {
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "from", referencedColumnName = "id", nullable = false)
	private UnitRef from;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "to", referencedColumnName = "id", nullable = false)
	private UnitRef to;

}
