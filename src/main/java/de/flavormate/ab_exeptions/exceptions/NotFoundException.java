package de.flavormate.ab_exeptions.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {

	private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

	public NotFoundException(Class<?> identifier) {
		super(identifier, STATUS);

		getResponse().put("message", String.format("%s not found!", identifier.getSimpleName()));
	}
}
