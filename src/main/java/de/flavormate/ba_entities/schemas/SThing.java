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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SThing {
  private String alternateName;

  private String description;

  @JsonDeserialize(using = SImageDeserializer.class)
  private List<URI> image;

  private String name;

  private URI url;

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(
        this, ToStringStyle.JSON_STYLE, false, false, true, null);
  }
}
