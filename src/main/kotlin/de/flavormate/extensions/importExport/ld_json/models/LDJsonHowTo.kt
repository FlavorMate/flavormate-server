/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import de.flavormate.extensions.importExport.ld_json.models.types.step.LDJsonStep
import de.flavormate.extensions.importExport.ld_json.serializers.LDJsonStepDeserializer
import java.time.Duration

// @JsonIgnoreProperties(ignoreUnknown = true)
open class LDJsonHowTo : LDJsonCreativeWork() {
  @JsonProperty("@type") override val type = "HowTo"

  var performTime: Duration = Duration.ZERO

  var prepTime: Duration = Duration.ZERO

  @JsonDeserialize(using = LDJsonStepDeserializer::class) var step: List<LDJsonStep> = listOf()

  var totalTime: Duration = Duration.ZERO

  var yield: String? = null
}
