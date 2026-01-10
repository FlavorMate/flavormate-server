/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.ld_json.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonSchema
import de.flavormate.extensions.importExport.ld_json.serializers.LDJsonImageDeserializer

open class LDJsonThing : LDJsonSchema(null) {
  @JsonProperty("@type") override val type: String = "Thing"

  var alternateName: String? = null

  var description: String? = null

  @JsonDeserialize(using = LDJsonImageDeserializer::class)
  @JsonProperty("image")
  var images: List<String> = listOf()

  var name: String? = null

  var url: String? = null
}
