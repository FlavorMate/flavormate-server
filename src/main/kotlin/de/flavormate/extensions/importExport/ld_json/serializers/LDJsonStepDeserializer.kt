/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.serializers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonSchema
import de.flavormate.extensions.importExport.ld_json.models.types.step.LDJsonHowToSection
import de.flavormate.extensions.importExport.ld_json.models.types.step.LDJsonHowToStep
import de.flavormate.extensions.importExport.ld_json.models.types.step.LDJsonStep

class LDJsonStepDeserializer : LDJsonDeserializer<List<LDJsonStep>>() {
  override fun handleArray(node: JsonNode): List<LDJsonStep>? =
    node.asSequence().mapNotNull(::handleNode).flatMap { it }.toList().takeIf { it.isNotEmpty() }

  override fun handleObject(node: JsonNode): List<LDJsonStep>? {
    val type = objectMapper.convertValue<LDJsonSchema>(node).type

    val step =
      when (type) {
        "Text",
        "HowToStep" -> objectMapper.convertValue<LDJsonHowToStep>(node)

        "HowToSection" -> objectMapper.convertValue<LDJsonHowToSection>(node)
        else -> null
      }

    return step?.let(::listOf)
  }

  override fun handleString(node: JsonNode): List<LDJsonStep>? =
    createStringList(node.textValue())?.map { LDJsonHowToStep(it, null) }

  override fun handleNumber(node: JsonNode): List<LDJsonStep>? =
    createStringList(node.textValue())?.map { LDJsonHowToStep(it, null) }
}
