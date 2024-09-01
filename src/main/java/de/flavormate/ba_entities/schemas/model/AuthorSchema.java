package de.flavormate.ba_entities.schemas.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthorSchema {
	@JsonProperty("@type")
	private final String type = "Person";

	private final String name;
}

