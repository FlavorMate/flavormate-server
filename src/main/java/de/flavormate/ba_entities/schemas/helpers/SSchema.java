/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.helpers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.properties.bind.DefaultValue;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SSchema(@JsonProperty("@type") @DefaultValue("") String type) {}
