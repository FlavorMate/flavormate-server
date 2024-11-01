/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.features.service;

import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.ad_configurations.flavormate.FeaturesConfig;
import de.flavormate.ba_entities.features.model.FeatureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeaturesService {

  private final FeaturesConfig featuresConfig;
  private final CommonConfig commonConfig;

  public FeatureResponse getFeaturesConfig() {
    return new FeatureResponse(commonConfig.getVersion().toString(), featuresConfig.getFeatures());
  }
}
