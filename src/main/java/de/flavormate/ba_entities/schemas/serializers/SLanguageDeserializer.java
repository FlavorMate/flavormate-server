/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.JsonException;
import de.flavormate.ba_entities.schemas.helpers.SLanguage;
import de.flavormate.ba_entities.schemas.helpers.SSchema;
import java.util.stream.StreamSupport;

public class SLanguageDeserializer extends JDeserializer<String> {
  @Override
  String handleArray(JsonNode node) throws JsonException {
    return StreamSupport.stream(node.spliterator(), false)
        .map(this::handleNode)
        .findFirst()
        .orElse(null);
  }

  @Override
  String handleObject(JsonNode node) throws JsonException {
    final var type = objectMapper.convertValue(node, SSchema.class).type();
    final var language =
        switch (type) {
          case "Language" -> objectMapper.convertValue(node, SLanguage.class).name();
          default -> throw new JsonException();
        };

    return cleanString(language);
  }

  @Override
  String handleString(JsonNode node) throws JsonException {
    final var raw = node.textValue();
    return cleanString(raw);
  }

  @Override
  String handleNumber(JsonNode node) throws JsonException {
    throw new JsonException();
  }
}
