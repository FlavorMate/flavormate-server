/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.models.types.step

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.schema.models.types.LDJsonSchema
import org.schema.serializers.LDJsonStepDeserializer

@JsonIgnoreProperties(ignoreUnknown = true)
data class LDJsonHowToSection(
  val name: String?,
  val position: Int?,
  @JsonDeserialize(using = LDJsonStepDeserializer::class) val itemListElement: List<LDJsonStep>,
) : LDJsonStep, LDJsonSchema("HowToSection") {
  override fun toStepList(): List<String> {
    val result: MutableList<String> = mutableListOf()

    name?.let { result.add(it) }

    for (step in itemListElement) {
      result.addAll(step.toStepList())
    }

    return result
  }

  override fun flatten(): List<LDJsonStep> {
    return itemListElement.flatMap { it.flatten() }
  }
}
