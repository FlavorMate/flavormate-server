package de.flavormate.ba_entities.features.service;

import de.flavormate.ad_configurations.features.FeatureConfig;
import de.flavormate.ba_entities.features.model.FeatureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class FeaturesService {

	private final FeatureConfig features;

	@Value("${flavorMate.app.version}")
	private String version;


	public FeatureResponse getFeatures() {
		return new FeatureResponse(version, features.getFeatures());
	}
}
