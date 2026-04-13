/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.services

import de.flavormate.extensions.importExport.models.common.IENutrition
import de.flavormate.extensions.importExport.models.ieRecipeDraft.*
import de.flavormate.features.category.daos.models.CategoryEntity
import de.flavormate.features.category.repositories.CategoryRepository
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftFileEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftIngredientGroupItemNutritionEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftServingEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupItemEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupItemEntity
import de.flavormate.features.recipeDraft.repositories.RecipeDraftFileRepository
import de.flavormate.features.recipeDraft.repositories.RecipeDraftRepository
import de.flavormate.features.unit.repositories.UnitLocalizedRepository
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.enums.ImageResolution
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.FileService
import de.flavormate.utils.ImageUtils
import de.flavormate.utils.MimeTypes
import de.flavormate.utils.NumberUtils
import io.quarkus.logging.Log
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import java.io.File
import java.time.Duration
import kotlin.io.path.name
import org.apache.commons.lang3.StringUtils

@RequestScoped
class IERecipeDraftConvertService(
  private val authorizationDetails: AuthorizationDetails,
  private val categoryRepository: CategoryRepository,
  private val unitLocalizedRepository: UnitLocalizedRepository,
  private val recipeDraftRepository: RecipeDraftRepository,
  private val fileRecipeDraftRepository: RecipeDraftFileRepository,
  private val fileService: FileService,
) {

  @Transactional
  fun convert(input: IERecipeDraft): RecipeDraftEntity {
    val language = input.language.value
    val recipe =
      RecipeDraftEntity.create(authorizationDetails.getSelf())
        .apply {
          this.cookTime = (input.cookTime ?: Duration.ZERO)
          this.course = input.course
          this.description = input.description
          this.diet = input.diet
          this.label = input.label
          this.prepTime = input.prepTime ?: Duration.ZERO
          this.restTime = input.restTime ?: Duration.ZERO
          this.serving = mapServing(input.serving)
          this.instructionGroups =
            input.instructionGroups.mapTo(mutableListOf()) { mapInstructionGroup(it, this) }
          this.ingredientGroups =
            input.ingredientGroups.mapTo(mutableListOf()) { mapIngredientGroup(it, this, language) }
          this.categories =
            input.categories.mapNotNullTo(mutableListOf()) { mapCategory(it, language) }
          this.tags = input.tags.toMutableList()
          this.url = input.url
        }
        .also {
          it.generateIndices()
          recipeDraftRepository.persist(it)

          for (category in it.categories) {
            category.recipeDrafts.add(it)
            categoryRepository.persist(category)
          }
        }
        .apply { this.files = input.files.mapNotNullTo(mutableListOf()) { mapFile(it, this) } }

    return recipe
  }

  private fun mapServing(input: IERecipeDraftServing): RecipeDraftServingEntity {
    return RecipeDraftServingEntity().apply {
      this.amount = input.amount
      this.label = input.label
    }
  }

  private fun mapInstructionGroup(
    input: IERecipeDraftInstructionGroup,
    recipe: RecipeDraftEntity,
  ): RecipeDraftInstructionGroupEntity {
    return RecipeDraftInstructionGroupEntity().apply {
      this.label = input.label
      this.index = input.index
      this.instructions = input.instructions.mapTo(mutableListOf()) { mapInstruction(it, this) }
      this.recipe = recipe
    }
  }

  private fun mapInstruction(
    input: IERecipeDraftInstructionGroupItem,
    group: RecipeDraftInstructionGroupEntity,
  ): RecipeDraftInstructionGroupItemEntity {
    return RecipeDraftInstructionGroupItemEntity.create(
      label = input.label,
      index = input.index,
      group = group,
    )
  }

  private fun mapIngredientGroup(
    input: IERecipeDraftIngredientGroup,
    recipe: RecipeDraftEntity,
    language: String,
  ): RecipeDraftIngredientGroupEntity {
    return RecipeDraftIngredientGroupEntity().apply {
      this.label = input.label
      this.index = input.index
      this.ingredients =
        input.ingredients.mapNotNullTo(mutableListOf()) { mapIngredient(it, this, language) }
      this.recipe = recipe
    }
  }

  private fun mapIngredient(
    input: IERecipeDraftIngredientGroupItem,
    group: RecipeDraftIngredientGroupEntity,
    language: String,
  ): RecipeDraftIngredientGroupItemEntity? {
    val formatted =
      StringUtils.trimToNull(input.label)?.let { NumberUtils.convertExtendedFractionString(it) }
        ?: return null

    val regex = "^(?:(\\d*[.,]?\\d+|\\d+)\\s+)?(?:(\\w+)\\s+)?(.+)$".toRegex()

    val results = regex.findAll(formatted).flatMapTo(mutableListOf()) { it.groupValues }

    val amount = results[1].toDoubleOrNull()

    val unit =
      StringUtils.trimToNull(results[2])
        ?.let { unitLocalizedRepository.findByLabelAndLanguage(it, language) }
        ?.firstResult()

    val label =
      when (unit) {
        null -> StringUtils.trimToNull("${results[2]} ${results[3]}")
        else -> StringUtils.trimToNull(results[3])
      } ?: return null

    return RecipeDraftIngredientGroupItemEntity().apply {
      this.amount = amount
      this.unit = unit
      this.label = label
      this.group = group
      this.nutrition = mapIngredientNutrition(input.nutrition)
    }
  }

  private fun mapIngredientNutrition(
    input: IENutrition?
  ): RecipeDraftIngredientGroupItemNutritionEntity {
    if (input == null) return RecipeDraftIngredientGroupItemNutritionEntity()

    return RecipeDraftIngredientGroupItemNutritionEntity().apply {
      this.openFoodFactsId = input.openFoodFactsId
      this.carbohydrates = input.carbohydrates
      this.energyKcal = input.energyKcal
      this.fat = input.fat
      this.saturatedFat = input.saturatedFat
      this.sugars = input.sugars
      this.fiber = input.fiber
      this.proteins = input.proteins
      this.salt = input.salt
      this.sodium = input.sodium
    }
  }

  private fun mapCategory(input: String, language: String): CategoryEntity? =
    categoryRepository.findByLocalizedLabel(input)?.also { it.translate(language) }

  private fun mapFile(input: File, draft: RecipeDraftEntity): RecipeDraftFileEntity? {
    var entity: RecipeDraftFileEntity? = null
    try {
      entity =
        RecipeDraftFileEntity.create(
            authorizationDetails.getSelf(),
            draft,
            ImageResolution.Original.fileName.name,
          )
          .apply { this.mimeType = MimeTypes.WEBP_MIME }
          .also { fileRecipeDraftRepository.persist(it) }

      val destination = fileService.createPath(FilePath.RecipeDraft, entity.id)

      ImageUtils.createOriginal(inputFile = input.toPath(), outputDir = destination)
    } catch (e: Exception) {
      Log.error("Failed to convert image ${input.path}", e)
    } finally {
      input.delete()
    }

    return entity
  }
}
