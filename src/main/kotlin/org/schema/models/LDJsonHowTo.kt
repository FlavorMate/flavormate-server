/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.time.Duration
import org.schema.models.types.step.LDJsonStep
import org.schema.serializers.LDJsonStepDeserializer

open class LDJsonHowTo : LDJsonCreativeWork() {
  @JsonProperty("@type") override val type = "HowTo"

  var performTime: Duration = Duration.ZERO

  var prepTime: Duration = Duration.ZERO

  @JsonDeserialize(using = LDJsonStepDeserializer::class) var step: List<LDJsonStep> = listOf()

  var totalTime: Duration = Duration.ZERO

  var yield: String? = null
}
