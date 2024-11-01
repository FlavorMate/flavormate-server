/* Licensed under AGPLv3 2024 */
package de.flavormate.ad_configurations.flavormate;

import java.net.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "flavormate.paths")
public record PathsConfig(URL backup, URL content, URL logs) {}
