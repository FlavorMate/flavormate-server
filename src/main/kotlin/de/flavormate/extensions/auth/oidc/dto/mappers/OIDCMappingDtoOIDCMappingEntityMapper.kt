/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.dto.mappers

import de.flavormate.extensions.auth.oidc.dao.models.OIDCMappingEntity
import de.flavormate.extensions.auth.oidc.dto.models.OIDCMappingDto

object OIDCMappingDtoOIDCMappingEntityMapper {
  fun mapNotNullBasic(input: OIDCMappingEntity): OIDCMappingDto {
    return OIDCMappingDto(
      issuer = input.provider.issuer,
      subject = input.subject,
      name = input.email ?: input.name ?: input.subject,
      createdOn = input.createdOn,
      providerId = input.provider.id,
      providerName = input.provider.label,
      icon = input.provider.icon,
    )
  }
}
