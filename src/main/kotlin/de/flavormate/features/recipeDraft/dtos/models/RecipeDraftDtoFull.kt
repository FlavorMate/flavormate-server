/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.dtos.models

import de.flavormate.features.account.dtos.models.AccountPreviewDto
import de.flavormate.features.category.dtos.models.CategoryDto
import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import java.time.Duration
import java.time.LocalDateTime

data class RecipeDraftDtoFull(
  override val id: String,
  override val version: Long,
  override val createdOn: LocalDateTime,
  override val lastModifiedOn: LocalDateTime,
  override val label: String? = null,
  override val originId: String? = null,
  val ownedBy: AccountPreviewDto,
  val description: String? = null,
  val prepTime: Duration,
  val cookTime: Duration,
  val restTime: Duration,
  val serving: RecipeDraftServingDto,
  val ingredientGroups: List<RecipeDraftIngredientGroupDto>,
  val instructionGroups: List<RecipeDraftInstructionGroupDto>,
  val categories: List<CategoryDto>,
  val tags: List<String>,
  val course: Course? = null,
  val diet: Diet? = null,
  val url: String? = null,
  val files: List<RecipeDraftFileDto>,
) : RecipeDraftDto
