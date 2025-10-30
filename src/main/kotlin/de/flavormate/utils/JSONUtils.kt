/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import jakarta.ws.rs.NotAcceptableException
import java.util.*
import java.util.function.Supplier

/**
 * Utility class for working with JSON data using the Jackson ObjectMapper. This class provides
 * methods to extract specific fields or lists from a JSON object.
 */
object JSONUtils {
  /** ObjectMapper instance for JSON serialization and deserialization. */
  val mapper: ObjectMapper =
    ObjectMapper()
      .findAndRegisterModules()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)

  /**
   * Extracts a specific field from a JSON object and converts it to the specified data type.
   *
   * @param <T> The target data type to convert the field to.
   * @param body The JSON object from which to extract the field.
   * @param fieldName The name of the field to extract.
   * @param clazz The class representing the target data type.
   * @return The extracted field converted to the specified data type or null if the field is
   *   missing. </T>
   */
  fun <T> extractField(body: JsonNode, fieldName: String?, clazz: Class<T>?): T? {
    return Optional.ofNullable(body[fieldName])
      .map { jsonNode: JsonNode? -> mapper.convertValue(jsonNode, clazz) }
      .orElse(null)
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
   *   if the field is missing. </T>
   */
  fun <T> extractList(body: JsonNode, fieldName: String?, clazz: Class<Array<T>>?): List<T> {
    return Optional.ofNullable(body[fieldName])
      .map { jsonNode: JsonNode? -> Arrays.asList(*mapper.convertValue(jsonNode, clazz)) }
      .orElse(emptyList())
  }

  /**
   * Extracts an object from a JSON field and converts it to the specified data type.
   *
   * @param <T> The target data type of the element.
   * @param body The JSON object containing the field.
   * @param clazz The class representing the target data type.
   * @return An element converted to the specified data type, or null if the field is missing. </T>
   */
  fun <T> parseObject(body: JsonNode?, clazz: Class<T>?): T {
    return Optional.ofNullable<JsonNode>(body)
      .map<T> { jsonNode: JsonNode? -> (mapper.convertValue<T>(jsonNode, clazz)) }
      .orElseThrow<RuntimeException>(Supplier<RuntimeException> { NotAcceptableException() })
  }

  @Throws(JsonProcessingException::class)
  fun toJsonString(`object`: Any?): String {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(`object`)
  }
}
