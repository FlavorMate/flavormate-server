/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.storyDraft.converters

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import de.flavormate.features.storyDraft.daos.models.StoryDraftRecipeEntity
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

/**
 * This class is responsible for converting between `Set<StoryDraftRecipeEntity>` and
 * `Set<Map<String, Any?>>` for persistence purposes. It implements the `AttributeConverter`
 * interface provided by JPA to handle custom serialization and deserialization of attributes.
 *
 * The converter is designed to serialize a set of `StoryDraftRecipeEntity` objects into a
 * JSON-compatible structure when persisting to the database and to deserialize it back into a set
 * of entity objects when reading from the database.
 *
 * The conversion process utilizes the Jackson library's `ObjectMapper` to handle the transformation
 * between the object types and JSON data structures.
 */
@Converter
class StoryDraftRecipeConverter : AttributeConverter<StoryDraftRecipeEntity, Map<String, Any?>> {
  override fun convertToDatabaseColumn(attribute: StoryDraftRecipeEntity?): Map<String, Any?>? {
    if (attribute == null) return null
    val om = ObjectMapper().findAndRegisterModules()

    return om.convertValue(attribute)
  }

  override fun convertToEntityAttribute(dbData: Map<String, Any?>?): StoryDraftRecipeEntity? {
    if (dbData == null) return null

    val om = ObjectMapper().findAndRegisterModules()

    return om.convertValue(dbData)
  }
}
