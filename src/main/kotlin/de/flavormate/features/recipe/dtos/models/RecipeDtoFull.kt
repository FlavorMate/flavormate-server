/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.dtos.models

import de.flavormate.features.account.dtos.models.AccountPreviewDto
import de.flavormate.features.category.dtos.models.CategoryDto
import de.flavormate.features.tag.dtos.models.TagDto
import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import java.time.Duration
import java.time.LocalDateTime

data class RecipeDtoFull(
  override val id: String,
  override val createdOn: LocalDateTime,
  override val label: String,
  override val prepTime: Duration,
  override val cookTime: Duration,
  override val restTime: Duration,
  override val diet: Diet,
  override val cover: RecipeFileDtoPreview?,
  val version: Long,
  val ownedBy: AccountPreviewDto,
  val description: String? = null,
  val serving: RecipeServingDto? = null,
  val instructionGroups: List<RecipeInstructionGroupDto>? = null,
  val ingredientGroups: List<RecipeIngredientGroupDto>? = null,
  val course: Course,
  val url: String? = null,
  val categories: List<CategoryDto>,
  val files: List<RecipeFileDtoPreview>,
  val tags: List<TagDto>,
) : RecipeDto
