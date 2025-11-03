/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.unit.dtos.models

data class UnitLocalizedDto(
  val id: String,
  val unitRef: UnitRefDto,
  val labelSg: String,
  val labelSgAbrv: String? = null,
  val labelPl: String? = null,
  val labelPlAbrv: String? = null,
)
