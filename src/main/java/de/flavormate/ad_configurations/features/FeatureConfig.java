package de.flavormate.ad_configurations.features;

import de.flavormate.ba_entities.features.model.Feature;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;


@ConfigurationProperties(prefix = "flavormate.features")
public record FeatureConfig(Feature registration, Feature importExport, Feature recovery, Feature shareRecipes,
                            Feature story) {


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

		if (shareRecipes.enabled()) {
			enabled.add("shareRecipes");
		}

		if (story.enabled()) {
			enabled.add("story");
		}

		return enabled;
	}
}
