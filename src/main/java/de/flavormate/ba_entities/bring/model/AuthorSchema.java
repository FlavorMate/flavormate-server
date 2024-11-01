/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.bring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthorSchema {
  @JsonProperty("@type")
  private final String type = "Person";

  private final String name;
}
