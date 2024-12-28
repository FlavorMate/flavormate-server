/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.JsonException;
import de.flavormate.ba_entities.schemas.helpers.SImage;
import de.flavormate.ba_entities.schemas.helpers.SSchema;
import java.net.URI;
import java.util.List;
import java.util.stream.StreamSupport;

public class SImageDeserializer extends JDeserializer<List<URI>> {
  @Override
  List<URI> handleArray(JsonNode node) throws JsonException {
    return StreamSupport.stream(node.spliterator(), false)
        .map(this::handleNode)
        .flatMap(List::stream)
        .toList();
  }

  @Override
  List<URI> handleObject(JsonNode node) throws JsonException {
    final var type = objectMapper.convertValue(node, SSchema.class).type();
    final var url =
        switch (type) {
          case "ImageObject" -> objectMapper.convertValue(node, SImage.class).url();
          default -> throw new JsonException();
        };

    return List.of(URI.create(url));
  }

  @Override
  List<URI> handleString(JsonNode node) throws JsonException {
    return List.of(URI.create(node.textValue()));
  }

  @Override
  List<URI> handleNumber(JsonNode node) throws JsonException {
    throw new JsonException();
  }
}
