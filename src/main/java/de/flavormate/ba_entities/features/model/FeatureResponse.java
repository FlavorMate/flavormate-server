/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.features.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FeatureResponse {
  private String version;
  private List<String> features;
}
