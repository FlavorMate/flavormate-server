/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.ld_json.serializers

import com.fasterxml.jackson.databind.JsonNode
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonRestrictedDiet

class LDJsonRestrictedDietDeserializer : LDJsonDeserializer<LDJsonRestrictedDiet>() {

  private val prefix = "https://schema.org/"

  override fun handleArray(node: JsonNode): LDJsonRestrictedDiet? =
    node.asSequence().mapNotNull(::handleNode).firstOrNull()

  override fun handleObject(node: JsonNode): LDJsonRestrictedDiet? = null

  override fun handleString(node: JsonNode): LDJsonRestrictedDiet? {
    if (!node.textValue().startsWith(prefix)) return null
    val rawValue = node.textValue().substring(prefix.length)
    return LDJsonRestrictedDiet.fromString(rawValue)
  }

  override fun handleNumber(node: JsonNode): LDJsonRestrictedDiet? = null
}
