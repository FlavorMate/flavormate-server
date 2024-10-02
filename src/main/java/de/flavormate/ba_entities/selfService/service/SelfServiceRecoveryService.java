package de.flavormate.ba_entities.selfService.service;

import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ad_configurations.FlavorMateConfig;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import de.flavormate.ba_entities.account.wrapper.ForcePasswordForm;
import de.flavormate.ba_entities.email.model.PasswordRecoveryEMail;
import de.flavormate.ba_entities.email.service.EmailService;
import de.flavormate.ba_entities.token.model.Token;
import de.flavormate.ba_entities.token.repository.TokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.AbstractMap;
import java.util.Map;

@ConditionalOnProperty(prefix = "flavormate.features.recovery", value = "enabled", havingValue = "true")
@RequiredArgsConstructor
@Service
public class SelfServiceRecoveryService {

	private final AccountRepository accountRepository;
	private final EmailService emailService;
	private final TemplateEngine templateEngine;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;


	public Boolean resetPassword(String mail) throws NotFoundException, MessagingException {
		var account = accountRepository.findByMail(mail)
				.orElseThrow(() -> new NotFoundException(Account.class));

		Token token = tokenRepository.save(Token.PasswordToken(account));

		Map<String, Object> map =
				Map.ofEntries(new AbstractMap.SimpleEntry<>("username", account.getUsername()),
						new AbstractMap.SimpleEntry<>("name", account.getDisplayName()),
						new AbstractMap.SimpleEntry<>("token", token.getToken()),
						new AbstractMap.SimpleEntry<>("backendUrl", FlavorMateConfig.getBackendUrl())

				);

		emailService.sendMail(new PasswordRecoveryEMail(account.getMail(), map));

		return true;
	}

	public String resetPasswordConfirm(String tokenId, ForcePasswordForm form) {
		var context = new Context();

		context.setVariable("backendUrl", FlavorMateConfig.getBackendUrl());

		try {
			Token token = tokenRepository.findByToken(tokenId)
					.orElseThrow(() -> new NotFoundException(Token.class));

			Account account = accountRepository.findById(token.getOwner().getId())
					.orElseThrow(() -> new NotFoundException(Account.class));

			account.setPassword(passwordEncoder.encode(form.password()));

			accountRepository.save(account);

			tokenRepository.deleteById(token.getId());

			return templateEngine.process("recovery/password-recovery-success.html", context);
		} catch (Exception e) {
			return templateEngine.process("recovery/password-recovery-failed.html", context);
		}
	}


	public String resetPasswordPage(String token) {
		var context = new Context();

		context.setVariable("token", token);

		context.setVariable("backendUrl", FlavorMateConfig.getBackendUrl());

		return templateEngine.process("recovery/password-recovery.html", context);
	}

	public String successPage() {
		var context = new Context();

		context.setVariable("backendUrl", FlavorMateConfig.getBackendUrl());

		return templateEngine.process("recovery/password-recovery-success.html", context);

	}
}
