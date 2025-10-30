/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.converters

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupEntity
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

/**
 * Converts a set of `RecipeDraftInstructionGroupEntity` objects to a set of maps for database
 * storage and vice versa.
 *
 * This converter is designed to facilitate the persistence of complex instruction group structures
 * as JSON within the database. It transforms the `RecipeDraftInstructionGroupEntity` objects into a
 * format suitable for storage and reconstructs them upon retrieval.
 *
 * Features:
 * - Leverages Jackson's `ObjectMapper` for efficient serialization and deserialization.
 * - Provides null safety by returning `null` when the input is `null`.
 * - Compatible with JPA's `@Convert` annotation for easy integration with entity fields.
 */
@Converter
class InstructionGroupDraftConverter :
  AttributeConverter<List<RecipeDraftInstructionGroupEntity>, List<Map<String, Any?>>> {
  override fun convertToDatabaseColumn(
    attribute: List<RecipeDraftInstructionGroupEntity>?
  ): List<Map<String, Any?>>? {
    if (attribute == null) return null
    val om = ObjectMapper().findAndRegisterModules()

    return om.convertValue(attribute)
  }

  override fun convertToEntityAttribute(
    dbData: List<Map<String, Any?>>?
  ): List<RecipeDraftInstructionGroupEntity>? {
    if (dbData == null) return null

    val om = ObjectMapper().findAndRegisterModules()

    return om.convertValue(dbData)
  }
}
