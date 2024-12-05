/* Licensed under AGPLv3 2024 */
package de.flavormate.ad_configurations.flavormate;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "flavormate.misc")
public record MiscConfig(int highlightDays, Duration shareDuration) {}
