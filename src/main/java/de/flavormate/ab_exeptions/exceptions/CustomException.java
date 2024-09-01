package de.flavormate.ab_exeptions.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class CustomException extends Exception {

	private final HashMap<String, Object> response;

	protected CustomException(Class<?> identifier, HttpStatus status) {

		response = new HashMap<String, Object>(Map.ofEntries(
				new AbstractMap.SimpleEntry<String, Integer>("statusCode", status.value()),
				new AbstractMap.SimpleEntry<String, String>("errorCode",
						String.format("01x%s", status.value()))));

		// TODO Class index (0>Foo<x404)
	}
}
