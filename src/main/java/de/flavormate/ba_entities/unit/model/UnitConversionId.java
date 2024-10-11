package de.flavormate.ba_entities.unit.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Embeddable
@Getter
public class UnitConversionId {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from", referencedColumnName = "id", nullable = false)
	private UnitRef from;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to", referencedColumnName = "id", nullable = false)
	private UnitRef to;

}
