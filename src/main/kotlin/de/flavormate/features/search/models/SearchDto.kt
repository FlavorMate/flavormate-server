/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.search.models

data class SearchDto(
  val id: String,
  val source: SearchFilter,
  val label: String,
  val cover: String?,
)
