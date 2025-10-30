/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.serializers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonPerson
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonSchema

class LDJsonPersonDeserializer : LDJsonDeserializer<LDJsonPerson>() {

  override fun handleArray(node: JsonNode): LDJsonPerson? =
    node.asSequence().mapNotNull(::handleNode).firstOrNull()

  override fun handleObject(node: JsonNode): LDJsonPerson? {
    val type = objectMapper.convertValue<LDJsonSchema>(node).type

    val person =
      when (type) {
        "Person",
        "Organization" -> objectMapper.convertValue<LDJsonPerson>(node)

        else -> null
      }

    return cleanString(person?.name)?.let {
      return LDJsonPerson(it)
    }
  }

  override fun handleString(node: JsonNode): LDJsonPerson? =
    cleanString(node.textValue())?.let {
      return LDJsonPerson(it)
    }

  override fun handleNumber(node: JsonNode): LDJsonPerson? = null
}
