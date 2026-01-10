/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.openFoodFacts.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class OFFDoubleDeserializer : JsonDeserializer<Double>() {
  override fun deserialize(jsonParser: JsonParser, context: DeserializationContext?): Double? {
    val value = jsonParser.doubleValue
    return if (jsonParser.doubleValue == 0.0) null else value
  }
}
