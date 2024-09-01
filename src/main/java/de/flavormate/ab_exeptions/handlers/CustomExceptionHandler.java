package de.flavormate.ab_exeptions.handlers;

import de.flavormate.ab_exeptions.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(ConflictException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public HashMap<String, Object> processValidationError(ConflictException ex) {
		return ex.getResponse();
	}

	@ExceptionHandler(ForbiddenException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public HashMap<String, Object> processValidationError(ForbiddenException ex) {
		return ex.getResponse();
	}

	@ExceptionHandler(GoneException.class)
	@ResponseStatus(HttpStatus.GONE)
	@ResponseBody
	public HashMap<String, Object> processValidationError(GoneException ex) {
		return ex.getResponse();
	}

	@ExceptionHandler(NotAcceptableException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ResponseBody
	public HashMap<String, Object> processValidationError(NotAcceptableException ex) {
		return ex.getResponse();
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public HashMap<String, Object> processValidationError(NotFoundException ex) {
		return ex.getResponse();
	}

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public HashMap<String, Object> processValidationError(UnauthorizedException ex) {
		return ex.getResponse();
	}
}


