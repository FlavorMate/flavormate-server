/* Licensed under AGPLv3 2024 */
package de.flavormate.ad_configurations.flavormate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConditionalOnProperty(
    prefix = "flavormate.features.recovery",
    value = "enabled",
    havingValue = "true")
@ConfigurationProperties(prefix = "flavormate.mail")
@EnableConfigurationProperties(MailConfig.class)
public record MailConfig(
    @NotEmpty String host,
    @NotNull int port,
    @NotNull boolean auth,
    @NotEmpty String username,
    @NotEmpty String password,
    @NotNull boolean starttls,
    @NotEmpty @Email String from) {}
