/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.JsonException;
import de.flavormate.ba_entities.schemas.helpers.SName;
import de.flavormate.ba_entities.schemas.helpers.SPerson;
import de.flavormate.ba_entities.schemas.helpers.SSchema;
import java.util.stream.StreamSupport;

public class SPersonDeserializer extends JDeserializer<SPerson> {
  @Override
  SPerson handleArray(JsonNode node) throws JsonException {
    return StreamSupport.stream(node.spliterator(), false)
        .map(this::handleNode)
        .findFirst()
        .orElse(null);
  }

  @Override
  SPerson handleObject(JsonNode node) throws JsonException {
    final var type = objectMapper.convertValue(node, SSchema.class).type();

    final var name =
        switch (type) {
          case "Person", "Organization" -> objectMapper.convertValue(node, SName.class).name();
          default -> throw new JsonException();
        };

    return fromString(name);
  }

  @Override
  SPerson handleString(JsonNode node) throws JsonException {
    final var raw = node.textValue();
    return fromString(raw);
  }

  @Override
  SPerson handleNumber(JsonNode node) throws JsonException {
    throw new JsonException();
  }

  private SPerson fromString(String raw) {
    final var cleaned = cleanString(raw);
    if (cleaned != null) {
      return new SPerson(cleaned);
    } else {
      return null;
    }
  }
}
