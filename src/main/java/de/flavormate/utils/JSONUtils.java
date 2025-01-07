/* Licensed under AGPLv3 2024 */
package de.flavormate.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotAcceptableException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for working with JSON data using the Jackson ObjectMapper. This class provides
 * methods to extract specific fields or lists from a JSON object.
 */
public class JSONUtils {

  /** ObjectMapper instance for JSON serialization and deserialization. */
  public static ObjectMapper mapper =
      new ObjectMapper()
          .findAndRegisterModules()
          .setSerializationInclusion(JsonInclude.Include.NON_NULL)
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);

  // Private constructor to prevent instantiation of the utility class.
  private JSONUtils() {}

  /**
   * Extracts a specific field from a JSON object and converts it to the specified data type.
   *
   * @param <T> The target data type to convert the field to.
   * @param body The JSON object from which to extract the field.
   * @param fieldName The name of the field to extract.
   * @param clazz The class representing the target data type.
   * @return The extracted field converted to the specified data type or null if the field is
   *     missing.
   */
  public static <T> T extractField(JsonNode body, String fieldName, Class<T> clazz) {
    return Optional.ofNullable(body.get(fieldName))
        .map(jsonNode -> JSONUtils.mapper.convertValue(jsonNode, clazz))
        .orElse(null);
  }

  /**
   * Extracts a list of objects from a JSON array field and converts them to the specified data
   * type.
   *
   * @param <T> The target data type of the list elements.
   * @param body The JSON object containing the array field.
   * @param fieldName The name of the array field to extract.
   * @param clazz The class representing the target data type (e.g., array type).
   * @return A List of objects with elements converted to the specified data type, or an empty list
   *     if the field is missing.
   */
  public static <T> List<T> extractList(JsonNode body, String fieldName, Class<T[]> clazz) {
    return Optional.ofNullable(body.get(fieldName))
        .map(jsonNode -> Arrays.asList(JSONUtils.mapper.convertValue(jsonNode, clazz)))
        .orElse(Collections.emptyList());
  }

  /**
   * Extracts an object from a JSON field and converts it to the specified data type.
   *
   * @param <T> The target data type of the element.
   * @param body The JSON object containing the field.
   * @param clazz The class representing the target data type.
   * @return An element converted to the specified data type, or null if the field is missing.
   */
  public static <T> T parseObject(JsonNode body, Class<T> clazz) throws CustomException {
    return Optional.ofNullable(body)
        .map(jsonNode -> (JSONUtils.mapper.convertValue(jsonNode, clazz)))
        .orElseThrow(() -> new NotAcceptableException(clazz));
  }

  public static String toJsonString(Object object) throws JsonProcessingException {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
  }
}
