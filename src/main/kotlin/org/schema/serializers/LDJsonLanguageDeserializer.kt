/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.serializers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import org.schema.models.types.LDJsonLanguage
import org.schema.models.types.LDJsonSchema

class LDJsonLanguageDeserializer : LDJsonDeserializer<String>() {

  override fun handleArray(node: JsonNode): String? {
    return node.firstNotNullOfOrNull(::handleNode)
  }

  override fun handleObject(node: JsonNode): String? {
    val type = objectMapper.convertValue<LDJsonSchema>(node).type

    val language =
      when (type) {
        "Language" -> objectMapper.convertValue<LDJsonLanguage>(node)
        else -> null
      }

    return cleanString(language?.name)
  }

  override fun handleString(node: JsonNode): String? = cleanString(node.textValue())

  override fun handleNumber(node: JsonNode): String? = null
}
