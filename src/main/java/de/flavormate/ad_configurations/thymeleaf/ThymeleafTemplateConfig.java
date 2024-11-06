/* Licensed under AGPLv3 2024 */
package de.flavormate.ad_configurations.thymeleaf;

import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafTemplateConfig {

  @Bean
  public SpringTemplateEngine springTemplateEngine() {
    SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
    springTemplateEngine.addTemplateResolver(emailTemplateResolver());
    springTemplateEngine.addTemplateResolver(htmlTemplateResolver());
    return springTemplateEngine;
  }

  public ClassLoaderTemplateResolver htmlTemplateResolver() {
    ClassLoaderTemplateResolver htmlTemplateResolver = new ClassLoaderTemplateResolver();
    htmlTemplateResolver.setPrefix("/html-templates/");
    htmlTemplateResolver.setSuffix(".html");
    htmlTemplateResolver.setTemplateMode(TemplateMode.HTML);
    htmlTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    htmlTemplateResolver.setCacheable(false);
    htmlTemplateResolver.setCheckExistence(true);
    return htmlTemplateResolver;
  }

  public ClassLoaderTemplateResolver emailTemplateResolver() {
    ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
    emailTemplateResolver.setPrefix("/mail-templates/");
    emailTemplateResolver.setSuffix(".html");
    emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
    emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    emailTemplateResolver.setCacheable(false);
    emailTemplateResolver.setCheckExistence(true);
    return emailTemplateResolver;
  }
}
