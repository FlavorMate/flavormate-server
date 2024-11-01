/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.selfService.service;

import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import de.flavormate.ba_entities.account.wrapper.ForcePasswordForm;
import de.flavormate.ba_entities.email.model.PasswordRecoveryMail;
import de.flavormate.ba_entities.email.service.MailService;
import de.flavormate.ba_entities.token.model.Token;
import de.flavormate.ba_entities.token.repository.TokenRepository;
import de.flavormate.bb_thymeleaf.Fragments;
import de.flavormate.bb_thymeleaf.MainPage;
import jakarta.mail.MessagingException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@ConditionalOnProperty(
    prefix = "flavormate.features.recovery",
    value = "enabled",
    havingValue = "true")
@RequiredArgsConstructor
@Service
public class SelfServiceRecoveryService {

  private final AccountRepository accountRepository;
  private final MailService mailService;
  private final TemplateEngine templateEngine;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final CommonConfig commonConfig;
  private final MessageSource messageSource;

  public Boolean resetPassword(String mail) throws NotFoundException, MessagingException {
    var account =
        accountRepository.findByMail(mail).orElseThrow(() -> new NotFoundException(Account.class));

    Token token = tokenRepository.save(Token.PasswordToken(account));

    Map<String, Object> data =
        Map.ofEntries(
            Map.entry("username", account.getUsername()),
            Map.entry("name", account.getDisplayName()),
            Map.entry("token", token.getToken()));

    var html =
        new MainPage(templateEngine, commonConfig).process(Fragments.RECOVERY_PASSWORD_MAIL, data);

    var passwordMail = new PasswordRecoveryMail(account.getMail(), messageSource);

    mailService.sendMail(passwordMail, html);

    return true;
  }

  public String resetPasswordConfirm(String tokenId, ForcePasswordForm form) {
    var site = new MainPage(templateEngine, commonConfig);
    try {
      Token token =
          tokenRepository
              .findByToken(tokenId)
              .orElseThrow(() -> new NotFoundException(Token.class));

      Account account =
          accountRepository
              .findById(token.getOwner().getId())
              .orElseThrow(() -> new NotFoundException(Account.class));

      account.setPassword(passwordEncoder.encode(form.password()));

      accountRepository.save(account);

      tokenRepository.deleteById(token.getId());

      return site.process(Fragments.RECOVERY_PASSWORD_OK, null);
    } catch (Exception e) {
      return site.process(Fragments.RECOVERY_PASSWORD_FAILED, null);
    }
  }

  public String resetPasswordPage(String token) {
    Map<String, Object> data = Map.ofEntries(Map.entry("token", token));

    return new MainPage(templateEngine, commonConfig)
        .process(Fragments.RECOVERY_PASSWORD_FORM, data);
  }
}
