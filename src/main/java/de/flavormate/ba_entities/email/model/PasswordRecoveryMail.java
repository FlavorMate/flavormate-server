package de.flavormate.ba_entities.email.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class PasswordRecoveryMail extends Mail {
	final private static String SUBJECT = "Zurücksetzen deines Passworts";
	final private static String TEMPLATE = "password-recover.html";

	public PasswordRecoveryMail(String to, Map<String, Object> properties) {
		super(to, SUBJECT, TEMPLATE, properties);
	}

}
