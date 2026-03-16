/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.time.Instant
import org.schema.models.types.LDJsonPerson
import org.schema.serializers.LDJsonDefinedTermDeserializer
import org.schema.serializers.LDJsonInstantDeserializer
import org.schema.serializers.LDJsonLanguageDeserializer
import org.schema.serializers.LDJsonPersonDeserializer

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
