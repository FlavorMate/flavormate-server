/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.email.bean;

import de.flavormate.ad_configurations.flavormate.MailConfig;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
    prefix = "flavormate.features.recovery",
    value = "enabled",
    havingValue = "true")
@Component
@RequiredArgsConstructor
public class CustomJavaMailSender {

  private final MailConfig mailConfig;

  @Bean
  public JavaMailSender getJavaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailConfig.host());
    mailSender.setPort(mailConfig.port());

    mailSender.setUsername(mailConfig.username());
    mailSender.setPassword(mailConfig.password());

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", mailConfig.auth());
    props.put("mail.smtp.starttls.enable", mailConfig.starttls());

    return mailSender;
  }
}
