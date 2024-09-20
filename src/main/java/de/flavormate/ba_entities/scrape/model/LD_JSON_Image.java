package de.flavormate.ba_entities.scrape.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LD_JSON_Image(String url) {
}
