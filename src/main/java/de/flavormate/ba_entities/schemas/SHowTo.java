/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.flavormate.ba_entities.schemas.serializers.SStepDeserializer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SHowTo extends SCreativeWork {
  @JsonProperty("@type")
  private final String type = "HowTo";

  private Duration performTime = Duration.ZERO;

  private Duration prepTime = Duration.ZERO;

  @JsonDeserialize(using = SStepDeserializer.class)
  private List<String> step = new ArrayList<>();

  private Duration totalTime = Duration.ZERO;

  private String yield;
}
