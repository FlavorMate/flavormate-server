package de.flavormate.ba_entities.scrape.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HowToSection extends LD_JSON_Instruction {
	// HowToSection may contain multiple HowToStep objects
}
