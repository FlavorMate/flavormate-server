package de.flavormate.ba_entities.email.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class PasswordRecoveryEMail extends EMail {
	final private static String SUBJECT = "Zurücksetzen deines Passworts";
	final private static String TEMPLATE = "password-recover.html";

	public PasswordRecoveryEMail(String to, Map<String, Object> properties) {
		super(to, SUBJECT, TEMPLATE, properties);
	}

}
