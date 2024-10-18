package de.flavormate.ad_configurations.flavormate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConditionalOnProperty(prefix = "flavormate.features.recovery", value = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "flavormate.mail")
public record MailConfig(
		@NotEmpty String host,
		@NotNull int port,
		@NotNull boolean auth,
		@NotEmpty String username,
		@NotEmpty String password,
		@NotNull boolean starttls,
		@NotEmpty String from) {
}
