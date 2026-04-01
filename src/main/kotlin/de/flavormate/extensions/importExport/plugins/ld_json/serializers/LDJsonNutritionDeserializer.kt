/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ld_json.serializers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.LDJsonNutritionInformation
import de.flavormate.extensions.importExport.plugins.ld_json.models.types.LDJsonSchema

class LDJsonNutritionDeserializer : LDJsonDeserializer<LDJsonNutritionInformation>() {

  override fun handleArray(node: JsonNode): LDJsonNutritionInformation? =
    node.firstNotNullOfOrNull(::handleNode)

  override fun handleObject(node: JsonNode): LDJsonNutritionInformation? {
    val type = objectMapper.convertValue<LDJsonSchema>(node).type

    val nutrition =
      when (type) {
        "NutritionInformation" -> objectMapper.convertValue<LDJsonNutritionInformation>(node)
        else -> null
      }

    return nutrition
  }

  override fun handleString(node: JsonNode): LDJsonNutritionInformation? = null

  override fun handleNumber(node: JsonNode): LDJsonNutritionInformation? = null
}
