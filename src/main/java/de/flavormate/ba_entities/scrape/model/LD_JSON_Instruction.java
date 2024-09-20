package de.flavormate.ba_entities.scrape.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Polymorphic handling of instructions (HowToStep or HowToSection)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = HowToStep.class, name = "HowToStep"),
		@JsonSubTypes.Type(value = HowToSection.class, name = "HowToSection")
})
@Getter
@Setter
public class LD_JSON_Instruction {
	private String name;
	private String text;
	private List<LD_JSON_Instruction> itemListElement; // If it's a section, it may contain multiple steps

}
