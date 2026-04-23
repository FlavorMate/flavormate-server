/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ldJson.serializers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import de.flavormate.extensions.importExport.plugins.ldJson.models.types.LDJsonLanguage
import de.flavormate.extensions.importExport.plugins.ldJson.models.types.LDJsonSchema

class LDJsonLanguageDeserializer : LDJsonDeserializer<String>() {
  override fun handleArray(node: JsonNode): String? = node.firstNotNullOfOrNull(::handleNode)

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
