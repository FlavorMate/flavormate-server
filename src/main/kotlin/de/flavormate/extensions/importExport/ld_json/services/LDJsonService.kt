/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.services

import de.flavormate.extensions.importExport.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.ld_json.models.types.LDJsonRestrictedDiet
import de.flavormate.features.category.daos.models.CategoryEntity
import de.flavormate.features.category.repositories.CategoryRepository
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftFileEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftServingEntity
import de.flavormate.features.recipeDraft.repositories.RecipeDraftFileRepository
import de.flavormate.features.recipeDraft.repositories.RecipeDraftRepository
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.enums.FilePath
import de.flavormate.shared.enums.ImageWideResolution
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.FileService
import de.flavormate.utils.ImageUtils
import de.flavormate.utils.MimeTypes
import jakarta.enterprise.context.RequestScoped
import jakarta.transaction.Transactional
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import java.net.URI
import kotlin.io.path.createTempFile

@RequestScoped
class LDJsonService(
  val authorizationDetails: AuthorizationDetails,
  val categoryRepository: CategoryRepository,
  val fileRecipeDraftRepository: RecipeDraftFileRepository,
  val fileService: FileService,
  val recipeDraftRepository: RecipeDraftRepository,
  val instructionService: LDJsonInstructionService,
  val ingredientService: LDJsonIngredientService,
) {

  @Transactional
  fun ldJsonRecipeToRecipeDraftEntity(input: LDJsonRecipe, language: String): RecipeDraftEntity {
    val language = input.inLanguage ?: language

    val recipeDraft =
      RecipeDraftEntity.create(authorizationDetails.getSelf())
        .apply {
          this.label = input.name ?: input.alternateName
          this.description = input.description ?: input.alternativeHeadline
          this.prepTime = input.prepTime
          this.cookTime = input.cookTime
          this.serving =
            input.recipeYield?.let(::mapYield)
              ?: input.yield?.let(::mapYield)
              ?: RecipeDraftServingEntity.create()
          this.instructionGroups =
            instructionService.mapInstructionGroupDrafts(input.recipeInstructions, this)
          this.ingredientGroups =
            ingredientService.mapIngredientGroupDrafts(input.recipeIngredient, language, this)
          this.categories =
            input.recipeCategory.mapNotNullTo(mutableListOf()) { mapCategory(it, language) }
            this.tags = input.keywords.filter(StringUtils::isNotBlank).map { it.lowercase() }.toMutableList()
          this.diet = mapDiet(input.suitableForDiet)
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

    saveImages(input.images, recipeDraft)

    return recipeDraft
  }

  fun mapCategory(input: String, language: String): CategoryEntity? =
      categoryRepository.findByLocalizedLabelAndLanguage(input)?.also { it.translate(language) }

  fun mapDiet(input: LDJsonRestrictedDiet?): Diet? =
    when (input) {
      LDJsonRestrictedDiet.VegetarianDiet -> Diet.Vegetarian
      LDJsonRestrictedDiet.VeganDiet -> Diet.Vegan
      else -> null
    }

  fun mapYield(input: String): RecipeDraftServingEntity {
    val parts = input.split("\\s+".toRegex()).filter(StringUtils::isNotBlank)
    if (parts.isEmpty()) return RecipeDraftServingEntity.create()

    val amount = parts.first().toDoubleOrNull() ?: return RecipeDraftServingEntity.create()
    if (amount <= 0) return RecipeDraftServingEntity.create()

    val label = parts.drop(1).joinToString(" ")

    return RecipeDraftServingEntity.create().apply {
      this.amount = amount
      this.label = label
    }
  }

  fun saveImages(images: List<String>, recipe: RecipeDraftEntity) {
    for (image in images) {
        try {
            val bytes = URI(image).toURL().readBytes()

            val tmpFile = createTempFile().toFile().also { it.deleteOnExit() }

            FileUtils.writeByteArrayToFile(tmpFile, bytes)

            val entity =
                RecipeDraftFileEntity.create(authorizationDetails.getSelf(), recipe)
                    .apply { this.mimeType = MimeTypes.WEBP_MIME }
                    .also { fileRecipeDraftRepository.persist(it) }

            val destination = fileService.createPath(FilePath.RecipeDraft, entity.id)

            for (entry in ImageWideResolution.entries) {
                if (entry == ImageWideResolution.Original)
                    ImageUtils.scaleMagick(
                        tmpFile.toPath(),
                        destination.resolve(entry.fileName),
                        entry.resolution,
                    )
                else
                    ImageUtils.resizeMagick(
                        tmpFile.toPath(),
                        destination.resolve(entry.fileName),
                        entry.resolution,
                    )
            }

            tmpFile.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
  }
}
