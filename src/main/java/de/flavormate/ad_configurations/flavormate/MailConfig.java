package de.flavormate.ad_configurations.flavormate;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "flavormate.mail")
public record MailConfig(String from) {
}
