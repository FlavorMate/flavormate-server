package de.flavormate.ba_entities.email.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public abstract class EMail {
	private final String to;
	private final String subject;
	private final String template;
	private final Map<String, Object> properties;

	@Value("${flavorMate.mail.from}")
	private String from;

}
