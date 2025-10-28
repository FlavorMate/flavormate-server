/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.dtos.models

import java.time.LocalDateTime

data class RecipeDraftDtoPreview(
  override val id: String,
  override val version: Long,
  override val createdOn: LocalDateTime,
  override val lastModifiedOn: LocalDateTime,
  override val label: String? = null,
  override val originId: String? = null,
) : RecipeDraftDto
