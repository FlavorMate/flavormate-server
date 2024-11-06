/* Licensed under AGPLv3 2024 */
package de.flavormate;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

import de.flavormate.aa_interfaces.converters.StringToVersionConverter;
import de.flavormate.ad_configurations.flavormate.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@EnableConfigurationProperties({
  AdminUserConfig.class,
  CommonConfig.class,
  FeaturesConfig.class,
  MiscConfig.class,
  PathsConfig.class,
  MailConfig.class,
})
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
