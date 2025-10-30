/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.serializers

import com.fasterxml.jackson.databind.JsonNode
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeParseException

class LDJsonInstantDeserializer : LDJsonDeserializer<Instant>() {

  override fun handleArray(node: JsonNode): Instant? = null

  override fun handleObject(node: JsonNode): Instant? = null

  override fun handleString(node: JsonNode): Instant? =
    parseAsInstant(node.textValue()) ?: parseAsLocalDate(node.textValue())

  override fun handleNumber(node: JsonNode): Instant? = null

  private fun parseAsInstant(input: String): Instant? {
    return try {
      Instant.parse(input)
    } catch (e: DateTimeParseException) {
      null
    }
  }

  private fun parseAsLocalDate(input: String, zone: ZoneOffset = ZoneOffset.UTC): Instant? {
    return try {
      LocalDate.parse(input).atStartOfDay(zone).toInstant()
    } catch (e: DateTimeParseException) {
      null
    }
  }
}
