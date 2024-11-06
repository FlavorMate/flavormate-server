/* Licensed under AGPLv3 2024 */
package de.flavormate.ad_configurations.flavormate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConditionalOnProperty(
    prefix = "flavormate.features.recovery",
    value = "enabled",
    havingValue = "true")
@ConfigurationProperties(prefix = "flavormate.mail")
public record MailConfig(
    String host,
    int port,
    boolean auth,
    String username,
    String password,
    boolean starttls,
    String from) {}
