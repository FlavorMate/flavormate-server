package de.flavormate.ba_entities.email.service;

import de.flavormate.ad_configurations.flavormate.MailConfig;
import de.flavormate.ba_entities.email.model.EMail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@ConditionalOnProperty(prefix = "flavormate.features.recovery", value = "enabled", havingValue = "true")
@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;

	private final MailConfig mailConfig;

	public void sendMail(EMail email) throws MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		Context context = new Context();
		context.setVariables(email.getProperties());
		helper.setFrom(mailConfig.from());
		helper.setTo(email.getTo());
		helper.setSubject(email.getSubject());
		String html = templateEngine.process(email.getTemplate(), context);
		helper.setText(html, true);
		mailSender.send(message);
	}
}
