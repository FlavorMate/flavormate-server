package de.flavormate.ad_configurations.flavormate;

import de.flavormate.aa_interfaces.models.Version;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

@Getter
@ConfigurationProperties(prefix = "flavormate.common")
public final class CommonConfig {
	private static URL BACKEND_URL;

	private final URL backendUrl;
	private final URL frontendUrl;
	private final String preferredLanguage;
	private final Version version;

	public CommonConfig(URL backendUrl, URL frontendUrl, String preferredLanguage, Version version) {
		this.backendUrl = backendUrl;
		this.frontendUrl = frontendUrl;
		this.preferredLanguage = preferredLanguage;
		this.version = version;

		BACKEND_URL = backendUrl;
	}

	public static URL backendUrl() {
		return BACKEND_URL;
	}
}
