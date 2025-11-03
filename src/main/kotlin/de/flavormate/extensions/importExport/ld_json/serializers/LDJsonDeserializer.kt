/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.serializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeType
import de.flavormate.configuration.jackson.CustomObjectMapper
import org.apache.commons.lang3.StringUtils

abstract class LDJsonDeserializer<T> : JsonDeserializer<T?>() {
  protected var objectMapper: ObjectMapper = CustomObjectMapper.instance

  override fun deserialize(jsonParser: JsonParser, context: DeserializationContext?): T? {
    val node = jsonParser.readValueAsTree<JsonNode>()
    return handleNode(node)
  }

  protected fun handleNode(node: JsonNode): T? {
    return when (node.nodeType) {
      JsonNodeType.ARRAY -> handleArray(node)
      JsonNodeType.OBJECT -> handleObject(node)
      JsonNodeType.STRING -> handleString(node)
      JsonNodeType.NUMBER -> handleNumber(node)
      else -> null
    }
  }

  abstract fun handleArray(node: JsonNode): T?

  abstract fun handleObject(node: JsonNode): T?

  abstract fun handleString(node: JsonNode): T?

  abstract fun handleNumber(node: JsonNode): T?

  protected fun cleanString(str: String?): String? =
    str?.replace("\\s+".toRegex(), " ").let(StringUtils::trimToNull)

  protected fun cleanStringList(list: List<String>): List<String>? =
    list.mapNotNull(::cleanString).takeIf { it.isNotEmpty() }

  protected fun createStringList(str: String?): List<String>? =
    str?.split("\\n+".toRegex())?.mapNotNull(::cleanString)?.takeIf { it.isNotEmpty() }
}
