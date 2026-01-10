/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.unit.dtos.mappers

import de.flavormate.features.unit.daos.models.UnitLocalizedEntity
import de.flavormate.features.unit.dtos.models.UnitLocalizedDto
import de.flavormate.shared.interfaces.BasicMapper

object UnitLocalizedDtoMapper : BasicMapper<UnitLocalizedEntity, UnitLocalizedDto>() {
  override fun mapNotNullBasic(input: UnitLocalizedEntity) =
    UnitLocalizedDto(
      id = input.id,
      unitRef = UnitRefDtoMapper.mapNotNullBasic(input.unitRef),
      labelSg = input.labelSg,
      labelSgAbrv = input.labelSgAbrv,
      labelPl = input.labelPl,
      labelPlAbrv = input.labelPlAbrv,
    )
}
