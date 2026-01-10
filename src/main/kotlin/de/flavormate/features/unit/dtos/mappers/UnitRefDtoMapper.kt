/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.unit.dtos.mappers

import de.flavormate.features.unit.daos.models.UnitRefEntity
import de.flavormate.features.unit.dtos.models.UnitRefDto
import de.flavormate.shared.interfaces.BasicMapper

object UnitRefDtoMapper : BasicMapper<UnitRefEntity, UnitRefDto>() {
  override fun mapNotNullBasic(input: UnitRefEntity) =
    UnitRefDto(id = input.id, description = input.description)
}
