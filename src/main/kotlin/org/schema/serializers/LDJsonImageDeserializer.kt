/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.serializers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import org.schema.models.types.LDJsonImageObject
import org.schema.models.types.LDJsonSchema

class LDJsonImageDeserializer : LDJsonDeserializer<List<String>>() {

  override fun handleArray(node: JsonNode): List<String>? =
    node.asSequence().mapNotNull(::handleNode).flatMap { it }.toList().takeIf { it.isNotEmpty() }

  override fun handleObject(node: JsonNode): List<String>? {
    val type = objectMapper.convertValue<LDJsonSchema>(node).type

    val image =
      when (type) {
        "ImageObject" -> objectMapper.convertValue<LDJsonImageObject>(node)
        else -> null
      }

    return cleanString(image?.url)?.let(::listOf)
  }

  override fun handleString(node: JsonNode): List<String>? =
    cleanString(node.textValue())?.let(::listOf)

  override fun handleNumber(node: JsonNode): List<String>? = null
}
