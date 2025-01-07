/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.helpers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record STextStep(String text) implements SStep {

  @Override
  public List<String> toStepList() {
    return List.of(text);
  }
}
