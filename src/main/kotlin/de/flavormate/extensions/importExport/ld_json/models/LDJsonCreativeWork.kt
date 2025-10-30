/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonPerson
import de.flavormate.extensions.importExport.ld_json.serializers.LDJsonDefinedTermDeserializer
import de.flavormate.extensions.importExport.ld_json.serializers.LDJsonInstantDeserializer
import de.flavormate.extensions.importExport.ld_json.serializers.LDJsonLanguageDeserializer
import de.flavormate.extensions.importExport.ld_json.serializers.LDJsonPersonDeserializer
import java.time.Instant

// @JsonIgnoreProperties(ignoreUnknown = true)
open class LDJsonCreativeWork : LDJsonThing() {
  @JsonProperty("@type") override val type = "CreativeWork"

  var alternativeHeadline: String? = null

  @JsonDeserialize(using = LDJsonPersonDeserializer::class) var author: LDJsonPerson? = null

  @JsonDeserialize(using = LDJsonInstantDeserializer::class) var dateCreated: Instant? = null

  @JsonDeserialize(using = LDJsonInstantDeserializer::class) var dateModified: Instant? = null

  @JsonDeserialize(using = LDJsonInstantDeserializer::class) var datePublished: Instant? = null

  @JsonDeserialize(using = LDJsonLanguageDeserializer::class) var inLanguage: String? = null

  @JsonDeserialize(using = LDJsonDefinedTermDeserializer::class)
  var keywords: List<String> = listOf()

  var text: String? = null
}
