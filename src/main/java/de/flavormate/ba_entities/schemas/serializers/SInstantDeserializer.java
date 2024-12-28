/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.JsonException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

public class SInstantDeserializer extends JDeserializer<Instant> {
  @Override
  public Instant deserialize(JsonParser p, DeserializationContext context) throws IOException {
    var result = Instant.now();

    var currentToken = p.currentToken();

    if (currentToken == JsonToken.VALUE_STRING) {
      // If it's a single string, just add it to the result
      final var raw = handleString(p);
      try {
        return Instant.parse(raw);
      } catch (DateTimeParseException e) {

      }
      try {
        return LocalDate.parse(raw).atStartOfDay(ZoneOffset.UTC).toInstant();
      } catch (DateTimeParseException e) {

      }
      context.reportInputMismatch(
          LocalDateTime.class, "Expected an ISO 8601 Date (Time) String, but got: " + currentToken);

    } else {
      // Handle unexpected types by throwing an error or logging
      context.reportInputMismatch(
          LocalDateTime.class, "Expected an ISO 8601 Date (Time) String, but got: " + currentToken);
    }

    return result;
  }

  @Override
  Instant handleArray(JsonNode node) throws JsonException {
    throw new JsonException();
  }

  @Override
  Instant handleObject(JsonNode node) throws JsonException {
    throw new JsonException();
  }

  @Override
  Instant handleString(JsonNode node) throws JsonException {
    final var raw = node.textValue();

    try {
      return Instant.parse(raw);
    } catch (DateTimeParseException e) {
      // If instant cant be parsed, try LocalDate
    }

    try {
      return LocalDate.parse(raw).atStartOfDay(ZoneOffset.UTC).toInstant();
    } catch (DateTimeParseException e) {
      // If LocalDate cant be parsed, throw an JsonException
    }

    throw new JsonException();
  }

  @Override
  Instant handleNumber(JsonNode node) throws JsonException {
    throw new JsonException();
  }

  private String handleString(JsonParser parser) throws IOException {
    return parser.getValueAsString();
  }
}
