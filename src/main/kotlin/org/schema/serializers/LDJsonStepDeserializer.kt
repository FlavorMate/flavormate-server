/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.serializers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import org.schema.models.types.LDJsonSchema
import org.schema.models.types.step.LDJsonHowToSection
import org.schema.models.types.step.LDJsonHowToStep
import org.schema.models.types.step.LDJsonStep

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
