package de.flavormate.ba_entities.features.model;

import org.springframework.boot.context.properties.bind.DefaultValue;


public record Feature(@DefaultValue("false") boolean enabled) {
}
