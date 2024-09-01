package de.flavormate.ab_exeptions.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends CustomException {

	private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

	public ForbiddenException(Class<?> identifier) {
		super(identifier, STATUS);

		getResponse().put("message",
				String.format("%s has no access!", identifier.getSimpleName()));
	}

}
