package de.flavormate.ad_configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FlavorMateConfig {


	@Getter
	private static String backendUrl;
	@Getter
	private static String frontendUrl;


	@Value("${flavorMate.base.backend}")
	private String backendRoot;

	@Value("${flavorMate.base.frontend}")
	private String frontendRoot;

	public void init() {
		backendUrl = backendRoot;
		frontendUrl = frontendRoot;
	}
}
