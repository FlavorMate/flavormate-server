package de.flavormate.ad_configurations.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import de.flavormate.ba_entities.scrape.model.LD_JSON_Image;

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

					// Deserialize LD_JSON_Image and extract the URL
					LD_JSON_Image LDJSONImage = p.readValueAs(LD_JSON_Image.class);
					result.add(LDJSONImage.url());
				}
			}
		} else if (currentToken == JsonToken.VALUE_STRING) {
			// If it's a single string, just add it to the result
			result.add(p.getValueAsString());
		} else if (currentToken == JsonToken.START_OBJECT) {
			// Deserialize LD_JSON_Image if it's an object
			LD_JSON_Image LDJSONImage = p.readValueAs(LD_JSON_Image.class);
			result.add(LDJSONImage.url());
		} else {
			// Handle unexpected types by throwing an error or logging
			ctxt.reportInputMismatch(
					List.class,
					"Expected either a string, array, or an LD_JSON_Image, but got: " + currentToken
			);
		}

		return result;
	}
}
