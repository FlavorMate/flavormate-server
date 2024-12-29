/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.flavormate.ba_entities.schemas.serializers.SImageDeserializer;
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
public class SThing {
  @JsonProperty("@Type")
  private final String type = "Thing";

  @JsonProperty("@Schema")
  private final String schema = "https://schema.org/";

  private String alternateName;

  private String description;

  @JsonDeserialize(using = SImageDeserializer.class)
  private List<String> image;

  private String name;

  private String url;
}
