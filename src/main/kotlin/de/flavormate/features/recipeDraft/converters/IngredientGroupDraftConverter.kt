/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.converters

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupEntity
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

/**
 * Converts a set of `RecipeDraftIngredientGroupEntity` objects to a set of maps for database
 * storage and vice versa.
 *
 * This converter facilitates storing complex ingredient group structures as JSON in the database by
 * transforming the `RecipeDraftIngredientGroupEntity` objects into a serializable format and
 * reconstructing them back into their Original form when retrieved.
 *
 * Features:
 * - Utilizes Jackson's `ObjectMapper` to handle serialization and deserialization.
 * - Ensures null safety by gracefully handling `null` values.
 * - Can be utilized with JPA's `@Convert` annotation to seamlessly integrate with database entity
 *   fields.
 */
@Converter
class IngredientGroupDraftConverter :
  AttributeConverter<List<RecipeDraftIngredientGroupEntity>, List<Map<String, Any?>>> {
  override fun convertToDatabaseColumn(
    attribute: List<RecipeDraftIngredientGroupEntity>?
  ): List<Map<String, Any?>>? {
    if (attribute == null) return null
    val om = ObjectMapper().findAndRegisterModules()

    return om.convertValue(attribute)
  }

  override fun convertToEntityAttribute(
    dbData: List<Map<String, Any?>>?
  ): List<RecipeDraftIngredientGroupEntity>? {
    if (dbData == null) return null

    val om = ObjectMapper().findAndRegisterModules()

    return om.convertValue(dbData)
  }
}
