/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.ld_json.serializers

import com.fasterxml.jackson.databind.JsonNode

class LDJsonStringDeserializer : LDJsonDeserializer<List<String>>() {

  override fun handleArray(node: JsonNode): List<String>? =
    node.asSequence().mapNotNull(::handleNode).flatMap { it }.toList().takeIf { it.isNotEmpty() }

  override fun handleObject(node: JsonNode): List<String>? = null

  override fun handleString(node: JsonNode): List<String>? = createStringList(node.textValue())

  override fun handleNumber(node: JsonNode): List<String>? = null
}
