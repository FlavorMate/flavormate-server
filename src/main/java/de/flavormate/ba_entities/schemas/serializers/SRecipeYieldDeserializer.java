/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.JsonException;
import java.util.stream.StreamSupport;

public class SRecipeYieldDeserializer extends JDeserializer<String> {
  @Override
  String handleArray(JsonNode node) throws JsonException {
    return StreamSupport.stream(node.spliterator(), false)
        .map(this::handleNode)
        .findFirst()
        .orElse("");
  }

  @Override
  String handleObject(JsonNode node) throws JsonException {
    throw new JsonException();
  }

  @Override
  String handleString(JsonNode node) throws JsonException {
    return node.textValue();
  }

  @Override
  String handleNumber(JsonNode node) throws JsonException {
    return node.numberValue().toString();
  }
}
