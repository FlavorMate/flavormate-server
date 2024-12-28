/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.JsonException;
import de.flavormate.ba_entities.schemas.helpers.SHowToSection;
import de.flavormate.ba_entities.schemas.helpers.SHowToStep;
import de.flavormate.ba_entities.schemas.helpers.SSchema;
import de.flavormate.ba_entities.schemas.helpers.STextStep;
import java.util.List;
import java.util.stream.StreamSupport;

public class SStepDeserializer extends JDeserializer<List<String>> {

  @Override
  List<String> handleArray(JsonNode nodeArr) {
    return StreamSupport.stream(nodeArr.spliterator(), false)
        .map(this::handleNode)
        .flatMap(List::stream)
        .toList();
  }

  @Override
  List<String> handleObject(JsonNode node) throws JsonException {

    final var type = objectMapper.convertValue(node, SSchema.class).type();
    final var list =
        switch (type) {
          case "HowToStep" -> objectMapper.convertValue(node, SHowToStep.class).toStepList();
          case "HowToSection" -> objectMapper.convertValue(node, SHowToSection.class).toStepList();
          case "Text" -> objectMapper.convertValue(node, STextStep.class).toStepList();
          default -> throw new JsonException();
        };

    return cleanStringList(list);
  }

  @Override
  List<String> handleString(JsonNode node) {
    return createStringList(node.textValue());
  }

  @Override
  List<String> handleNumber(JsonNode node) {
    return createStringList(node.numberValue().toString());
  }
}
