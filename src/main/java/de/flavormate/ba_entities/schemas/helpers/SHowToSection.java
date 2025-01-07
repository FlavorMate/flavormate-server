/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.helpers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SHowToSection(String name, int position, List<SStep> itemListElement)
    implements SStep {

  @Override
  public List<String> toStepList() {
    List<String> result = new ArrayList<>();
    if (name != null) {
      result.add(name);
    }
    if (itemListElement != null) {
      for (var step : itemListElement) {
        result.addAll(step.toStepList());
      }
    }
    return result;
  }
}
