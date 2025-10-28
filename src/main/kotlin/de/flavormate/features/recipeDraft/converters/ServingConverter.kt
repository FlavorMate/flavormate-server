/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.converters

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftServingEntity
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

/**
 * Converts `RecipeDraftServingEntity` objects to a map for database storage and vice versa.
 *
 * This converter is used to facilitate storing complex serving draft structures as JSON in the
 * database by transforming `RecipeDraftServingEntity` objects into a serializable format and
 * reconstructing them back into their Original form when retrieved.
 *
 * Key features:
 * - Utilizes Jackson's `ObjectMapper` for serialization and deserialization.
 * - Handles `null` values by returning `null` for both directions of conversion.
 * - Can be applied with JPA's `@Convert` annotation for seamless integration with entity fields.
 */
@Converter
class ServingConverter : AttributeConverter<RecipeDraftServingEntity, Map<String, Any?>> {
  override fun convertToDatabaseColumn(attribute: RecipeDraftServingEntity?): Map<String, Any?>? {
    if (attribute == null) return null
    val om = ObjectMapper().findAndRegisterModules()

    return om.convertValue(attribute)
  }

  override fun convertToEntityAttribute(dbData: Map<String, Any?>?): RecipeDraftServingEntity? {
    if (dbData == null) return null

    val om = ObjectMapper().findAndRegisterModules()

    return om.convertValue(dbData)
  }
}
