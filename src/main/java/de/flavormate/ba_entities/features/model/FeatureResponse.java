package de.flavormate.ba_entities.features.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FeatureResponse {
	private String version;
	private List<String> features;
}
