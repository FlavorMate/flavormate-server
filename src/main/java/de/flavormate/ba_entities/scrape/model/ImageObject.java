package de.flavormate.ba_entities.scrape.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ImageObject(String url) {
}
