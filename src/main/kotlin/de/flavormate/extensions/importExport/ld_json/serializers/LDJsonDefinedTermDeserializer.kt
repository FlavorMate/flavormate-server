/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.serializers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonDefinedTerm
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonSchema

class LDJsonDefinedTermDeserializer : LDJsonDeserializer<List<String>>() {

  override fun handleArray(node: JsonNode): List<String>? =
    node.asSequence().mapNotNull(::handleNode).flatMap { it }.toList().takeIf { it.isNotEmpty() }

  override fun handleObject(node: JsonNode): List<String>? {
    val type = objectMapper.convertValue<LDJsonSchema>(node).type

    val definedTerm =
      when (type) {
        "DefinedTerm" -> objectMapper.convertValue<LDJsonDefinedTerm>(node)
        else -> null
      }

    return createStringList(definedTerm?.termCode)
  }

  override fun handleString(node: JsonNode): List<String>? =
    cleanStringList(node.textValue().split(","))

  override fun handleNumber(node: JsonNode): List<String>? = null
}
