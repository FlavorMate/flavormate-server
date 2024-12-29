/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.JsonException;
import de.flavormate.ba_entities.schemas.helpers.SImage;
import de.flavormate.ba_entities.schemas.helpers.SSchema;
import java.util.List;
import java.util.stream.StreamSupport;

public class SImageDeserializer extends JDeserializer<List<String>> {
  @Override
  List<String> handleArray(JsonNode node) throws JsonException {
    return StreamSupport.stream(node.spliterator(), false)
        .map(this::handleNode)
        .flatMap(List::stream)
        .toList();
  }

  @Override
  List<String> handleObject(JsonNode node) throws JsonException {
    final var type = objectMapper.convertValue(node, SSchema.class).type();
    final var url =
        switch (type) {
          case "ImageObject" -> objectMapper.convertValue(node, SImage.class).url();
          default -> throw new JsonException();
        };

    return List.of(url);
  }

  @Override
  List<String> handleString(JsonNode node) throws JsonException {
    return List.of(node.textValue());
  }

  @Override
  List<String> handleNumber(JsonNode node) throws JsonException {
    throw new JsonException();
  }
}
