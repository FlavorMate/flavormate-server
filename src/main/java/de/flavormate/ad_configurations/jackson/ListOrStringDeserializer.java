package de.flavormate.ad_configurations.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListOrStringDeserializer extends JsonDeserializer<List<Object>> {

	@Override
	public List<Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		List<Object> result = new ArrayList<>();

		JsonToken currentToken = p.currentToken();
		if (currentToken == JsonToken.START_ARRAY) {
			// If the token is the start of an array, read it as a list
			result = p.readValueAs(List.class);
		} else if (currentToken == JsonToken.VALUE_STRING) {
			// If the token is a single string, add it to the list
			result.add(p.getValueAsString());
		} else {
			// Handle unexpected cases by throwing an exception
			ctxt.reportInputMismatch(
					List.class,
					"Expected either an array or a string for recipeIngredient or recipeInstructions, but got: " + currentToken
			);
		}
		return result;
	}
}
