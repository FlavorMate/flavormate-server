/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.flavormate.ab_exeptions.exceptions.JsonException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public abstract class JDeserializer<T> extends JsonDeserializer<T> {
  protected ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Override
  public T deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
    JsonNode node = jsonParser.readValueAsTree();

    return handleNode(node);
  }

  protected T handleNode(JsonNode node) {
    try {
      if (node.isArray()) {
        return handleArray(node);
      } else if (node.isObject()) {
        return handleObject(node);
      } else if (node.isTextual()) {
        return handleString(node);
      } else if (node.isNumber()) {
        return handleNumber(node);
      } else {
        throw new JsonException();
      }
    } catch (JsonException e) {
      log.error(
          "Expected either an Array, Object, String or Number but got: {}", node.toPrettyString());
      return null;
    }
  }

  abstract T handleArray(JsonNode node) throws JsonException;

  abstract T handleObject(JsonNode node) throws JsonException;

  abstract T handleString(JsonNode node) throws JsonException;

  abstract T handleNumber(JsonNode node) throws JsonException;

  protected String cleanString(String str) {
    str = str.replaceAll("\\s+", " ");
    return StringUtils.trimToNull(str);
  }

  protected List<String> cleanStringList(List<String> list) {
    return list.stream().map(this::cleanString).filter(Objects::nonNull).toList();
  }

  protected List<String> createStringList(String str) {
    final var raw = cleanString(str);
    return raw == null ? List.of() : List.of(raw);
  }
}
