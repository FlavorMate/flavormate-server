/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.flavormate.ba_entities.schemas.serializers.SImageDeserializer;
import java.net.URI;
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
  //	private String additionalType;

  private String alternateName;

  private String description;

  //	private String disambiguatingDescription;

  //	private String identifier;

  @JsonDeserialize(using = SImageDeserializer.class)
  private List<String> image;

  //	private String mainEntityOfPage;

  private String name;

  //	private String potentialAction;
  //
  //	private String sameAs;
  //
  //	private String subjectOf;

  private URI url;
}
