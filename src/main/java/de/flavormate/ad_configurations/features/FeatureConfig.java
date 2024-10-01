package de.flavormate.ad_configurations.features;

import de.flavormate.aa_interfaces.models.Feature;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "flavormate.features")
public record FeatureConfig(Feature registration, Feature importExport, Feature resetPassword, Feature shareRecipes) {
}
