package de.flavormate.ad_configurations.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import de.flavormate.ba_entities.scrape.model.ImageObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageDeserializer extends JsonDeserializer<List<String>> {
	@Override
	public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		List<String> result = new ArrayList<>();

		JsonToken currentToken = p.currentToken();
		if (currentToken == JsonToken.START_ARRAY) {
			// If the token is the start of an array, iterate through it
			while (p.nextToken() != JsonToken.END_ARRAY) {
				if (p.currentToken() == JsonToken.VALUE_STRING) {
					// Add string images to the list
					result.add(p.getValueAsString());
				} else if (p.currentToken() == JsonToken.START_OBJECT) {

					// Deserialize ImageObject and extract the URL
					ImageObject imageObject = p.readValueAs(ImageObject.class);
					result.add(imageObject.url());
				}
			}
		} else if (currentToken == JsonToken.VALUE_STRING) {
			// If it's a single string, just add it to the result
			result.add(p.getValueAsString());
		} else if (currentToken == JsonToken.START_OBJECT) {
			// Deserialize ImageObject if it's an object
			ImageObject imageObject = p.readValueAs(ImageObject.class);
			result.add(imageObject.url());
		} else {
			// Handle unexpected types by throwing an error or logging
			ctxt.reportInputMismatch(
					List.class,
					"Expected either a string, array, or an ImageObject, but got: " + currentToken
			);
		}

		return result;
	}
}
