/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.book.dtos.models

import de.flavormate.features.account.dtos.models.AccountPreviewDto
import de.flavormate.features.recipe.dtos.models.RecipeFileDtoPreview
import java.time.LocalDateTime

data class BookDto(
  val id: String,
  val version: Long,
  val createdOn: LocalDateTime,
  val lastModifiedOn: LocalDateTime,
  val ownedBy: AccountPreviewDto,
  val label: String,
  val visible: Boolean,
  val cover: RecipeFileDtoPreview?,
  val recipeCount: Int,
  val subscriberCount: Int,
)
