package de.flavormate.ab_exeptions.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends CustomException {

	private static final HttpStatus STATUS = HttpStatus.CONFLICT;

	public ConflictException(Class<?> identifier) {
		super(identifier, STATUS);

		getResponse().put("message",
				String.format("%s is conflicting!", identifier.getSimpleName()));
	}

}
