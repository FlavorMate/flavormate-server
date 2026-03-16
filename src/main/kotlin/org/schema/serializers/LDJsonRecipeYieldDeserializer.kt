/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.serializers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import jakarta.json.JsonException
import org.schema.models.types.LDJsonQuantitativeValue
import org.schema.models.types.LDJsonSchema

class LDJsonRecipeYieldDeserializer : LDJsonDeserializer<String>() {

  override fun handleArray(node: JsonNode): String? =
    node.asSequence().mapNotNull(::handleNode).firstOrNull()

  override fun handleObject(node: JsonNode): String? {
    val type = objectMapper.convertValue<LDJsonSchema>(node).type

    val quantitativeValue =
      when (type) {
        "QuantitativeValue" -> objectMapper.convertValue<LDJsonQuantitativeValue>(node)
        else -> null
      }

    return cleanString(quantitativeValue?.value)
  }

  @Throws(JsonException::class)
  override fun handleString(node: JsonNode): String? {
    val raw = node.textValue()
    return cleanString(raw)
  }

  @Throws(JsonException::class)
  override fun handleNumber(node: JsonNode): String {
    return node.numberValue().toString()
  }
}
