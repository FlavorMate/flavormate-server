package de.flavormate.ad_configurations.flavormate;

import de.flavormate.aa_interfaces.models.Version;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

@ConfigurationProperties(prefix = "flavormate.common")
public record CommonConfig(URL backendUrl, URL frontendUrl, String preferredLanguage, Version version) {
}
