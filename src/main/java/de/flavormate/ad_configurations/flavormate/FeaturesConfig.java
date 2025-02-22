/* Licensed under AGPLv3 2024 */
package de.flavormate.ad_configurations.flavormate;

import de.flavormate.ba_entities.features.model.Feature;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "flavormate.features")
public record FeaturesConfig(
    Feature registration,
    Feature importExport,
    Feature recovery,
    Feature story,
    Feature bring,
    Feature openFoodFacts,
    Feature share) {

  public List<String> getFeatures() {
    var enabled = new ArrayList<String>();

    if (registration.enabled()) {
      enabled.add("registration");
    }

    if (importExport.enabled()) {
      enabled.add("importExport");
    }

    if (recovery.enabled()) {
      enabled.add("recovery");
    }

    if (story.enabled()) {
      enabled.add("story");
    }

    if (bring.enabled()) {
      enabled.add("bring");
    }

    if (openFoodFacts.enabled()) {
      enabled.add("open-food-facts");
    }

    if (share.enabled()) {
      enabled.add("share");
    }

    return enabled;
  }
}
