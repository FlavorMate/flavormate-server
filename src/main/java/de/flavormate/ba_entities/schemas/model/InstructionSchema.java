package de.flavormate.ba_entities.schemas.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InstructionSchema {

	@JsonProperty("@type")
	private final String type = "HowToStep";

	private final String text;
}
