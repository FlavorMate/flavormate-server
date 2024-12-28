/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.JsonException;
import de.flavormate.ba_entities.schemas.helpers.SDefinedTerm;
import de.flavormate.ba_entities.schemas.helpers.SSchema;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;

public class SKeywordDeserializer extends JDeserializer<List<String>> {
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

    final var keyword =
        switch (type) {
          case "DefinedTerm" -> objectMapper.convertValue(node, SDefinedTerm.class).termCode();
          default -> throw new JsonException();
        };

    return List.of(keyword);
  }

  @Override
  List<String> handleString(JsonNode node) throws JsonException {
    final var raw = node.textValue();
    return Stream.of(raw.split(",")).map(StringUtils::trimToNull).filter(Objects::nonNull).toList();
  }

  @Override
  List<String> handleNumber(JsonNode node) throws JsonException {
    throw new JsonException();
  }
}
