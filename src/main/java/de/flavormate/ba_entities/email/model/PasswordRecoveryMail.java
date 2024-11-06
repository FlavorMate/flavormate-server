/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.email.model;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@Getter
public class PasswordRecoveryMail extends Mail {

  public PasswordRecoveryMail(String to, MessageSource messageSource) {
    super(
        to,
        messageSource.getMessage(
            "html-password_recovery_mail-subject", null, LocaleContextHolder.getLocale()));
  }
}
