package de.flavormate.ba_entities.email.service;

import de.flavormate.ad_configurations.flavormate.MailConfig;
import de.flavormate.ba_entities.email.model.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@ConditionalOnProperty(prefix = "flavormate.features.recovery", value = "enabled", havingValue = "true")
@Service
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender mailSender;
	private final MailConfig mailConfig;

	public void sendMail(Mail mail, String html) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(
				message,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name()
		);
		helper.setFrom(mailConfig.from());
		helper.setTo(mail.getTo());
		helper.setSubject(mail.getSubject());
		helper.setText(html, true);

		mailSender.send(message);
	}
}
