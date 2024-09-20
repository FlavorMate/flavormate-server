package de.flavormate.ba_entities.scrape.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HowToStep extends LD_JSON_Instruction {
	private String url;
	private String image;


}
