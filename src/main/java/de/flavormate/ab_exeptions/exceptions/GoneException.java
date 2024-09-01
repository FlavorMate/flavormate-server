package de.flavormate.ab_exeptions.exceptions;

import org.springframework.http.HttpStatus;

public class GoneException extends CustomException {

	private static final HttpStatus STATUS = HttpStatus.GONE;

	public GoneException(Class<?> identifier) {
		super(identifier, STATUS);

		getResponse().put("message", String.format("%s is gone!", identifier.getSimpleName()));
	}

}
