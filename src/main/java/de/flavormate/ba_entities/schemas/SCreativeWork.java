/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.flavormate.ba_entities.schemas.helpers.SPerson;
import de.flavormate.ba_entities.schemas.serializers.SInstantDeserializer;
import de.flavormate.ba_entities.schemas.serializers.SLanguageDeserializer;
import de.flavormate.ba_entities.schemas.serializers.SPersonDeserializer;
import de.flavormate.ba_entities.schemas.serializers.StringListCommaDeserializer;
import java.time.Instant;
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
public class SCreativeWork extends SThing {
  @JsonProperty("@type")
  private final String type = "CreativeWork";

  private String alternativeHeadline;

  @JsonDeserialize(using = SPersonDeserializer.class)
  private SPerson author;

  @JsonDeserialize(using = SInstantDeserializer.class)
  private Instant dateCreated;

  @JsonDeserialize(using = SInstantDeserializer.class)
  private Instant dateModified;

  @JsonDeserialize(using = SInstantDeserializer.class)
  private Instant datePublished;

  @JsonDeserialize(using = SLanguageDeserializer.class)
  private String inLanguage;

  @JsonDeserialize(using = StringListCommaDeserializer.class)
  private List<String> keywords;

  private String text;
}
