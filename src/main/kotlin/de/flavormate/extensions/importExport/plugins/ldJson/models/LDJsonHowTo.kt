/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ldJson.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import de.flavormate.extensions.importExport.plugins.ldJson.models.types.step.LDJsonStep
import de.flavormate.extensions.importExport.plugins.ldJson.serializers.LDJsonStepDeserializer
import java.time.Duration

open class LDJsonHowTo : LDJsonCreativeWork() {
  @JsonProperty("@type") override val type = "HowTo"

  var performTime: Duration = Duration.ZERO

  var prepTime: Duration = Duration.ZERO

  @JsonDeserialize(using = LDJsonStepDeserializer::class) var step: List<LDJsonStep> = listOf()

  var totalTime: Duration = Duration.ZERO

  var yield: String? = null
}
