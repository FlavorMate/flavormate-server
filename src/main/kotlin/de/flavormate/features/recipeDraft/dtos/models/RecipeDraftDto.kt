/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.dtos.models

import java.time.LocalDateTime

interface RecipeDraftDto {
  val id: String
  val version: Long
  val createdOn: LocalDateTime
  val lastModifiedOn: LocalDateTime
  val label: String?
  val originId: String?
}
