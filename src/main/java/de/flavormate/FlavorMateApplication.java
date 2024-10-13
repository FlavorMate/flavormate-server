package de.flavormate;

import de.flavormate.aa_interfaces.converters.StringToVersionConverter;
import de.flavormate.ad_configurations.flavormate.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableScheduling
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@EnableConfigurationProperties({AdminUserConfig.class, CommonConfig.class, FeaturesConfig.class, MailConfig.class, MiscConfig.class, PathsConfig.class})

public class FlavorMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlavorMateApplication.class, args);
	}

	@Bean
	@ConfigurationPropertiesBinding
	public StringToVersionConverter stringToVersionConverter() {
		return new StringToVersionConverter();
	}
}


