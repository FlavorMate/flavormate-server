package de.flavormate.ad_configurations.flavormate;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

@ConfigurationProperties(prefix = "flavormate.paths")
public record PathsConfig(URL backup, URL content, URL logs) {
}
