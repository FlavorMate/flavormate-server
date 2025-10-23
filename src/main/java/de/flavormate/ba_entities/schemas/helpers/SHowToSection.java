/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.helpers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.flavormate.ba_entities.schemas.serializers.SStepDeserializer;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SHowToSection(
    String name,
    int position,
    @JsonDeserialize(using = SStepDeserializer.class) List<String> itemListElement)
    implements SStep {

  @Override
  public List<String> toStepList() {
    List<String> result = new ArrayList<>();
    if (name != null) {
      result.add(name);
    }
    if (itemListElement != null) {
      result.addAll(itemListElement);
    }
    return result;
  }
}
