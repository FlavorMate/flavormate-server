/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.JsonException;
import de.flavormate.ba_entities.schemas.helpers.SOrganizationPerson;
import de.flavormate.ba_entities.schemas.helpers.SSchema;
import java.util.stream.StreamSupport;

public class SOrganizationPersonDeserializer extends JDeserializer<String> {
  @Override
  String handleArray(JsonNode node) throws JsonException {
    return StreamSupport.stream(node.spliterator(), false)
        .map(this::handleNode)
        .findFirst()
        .orElse("");
  }

  @Override
  String handleObject(JsonNode node) throws JsonException {
    final var type = objectMapper.convertValue(node, SSchema.class).type();

    return switch (type) {
      case "Person", "Organization" ->
          objectMapper.convertValue(node, SOrganizationPerson.class).name();
      default -> throw new JsonException();
    };
  }

  @Override
  String handleString(JsonNode node) throws JsonException {
    return node.textValue();
  }

  @Override
  String handleNumber(JsonNode node) throws JsonException {
    throw new JsonException();
  }
}
