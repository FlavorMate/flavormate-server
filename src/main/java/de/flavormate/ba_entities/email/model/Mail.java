package de.flavormate.ba_entities.email.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public abstract class Mail {
	private final String to;
	private final String subject;
	private final String template;
	private final Map<String, Object> properties;
}
