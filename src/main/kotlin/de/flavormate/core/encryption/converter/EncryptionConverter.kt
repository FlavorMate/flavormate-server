/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.encryption.converter

import de.flavormate.core.encryption.services.EncryptionService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@ApplicationScoped
@Converter
class EncryptionConverter(private val encryptionService: EncryptionService) :
  AttributeConverter<String, String> {
  override fun convertToDatabaseColumn(attribute: String?): String? {
    return attribute?.let { encryptionService.encrypt(it) }
  }

  override fun convertToEntityAttribute(dbData: String?): String? {
    return dbData?.let { encryptionService.decrypt(it) }
  }
}
