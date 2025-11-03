/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.share.mappers

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.extensions.share.controllers.ShareController
import de.flavormate.extensions.share.models.SharedIngredientGroup
import de.flavormate.extensions.share.models.SharedInstructionGroup
import de.flavormate.extensions.share.models.SharedRecipe
import de.flavormate.features.category.daos.models.CategoryEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipe.daos.models.ServingEntity
import de.flavormate.features.recipe.daos.models.ingredient.IngredientEntity
import de.flavormate.features.recipe.daos.models.ingredient.IngredientGroupEntity
import de.flavormate.features.recipe.daos.models.instruction.InstructionGroupEntity
import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.enums.ImageWideResolution
import de.flavormate.shared.interfaces.BasicMapper
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.shared.services.TemplateService
import de.flavormate.utils.DurationUtils
import de.flavormate.utils.NumberUtils
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.core.UriBuilder
import java.net.URI
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import org.apache.commons.lang3.StringUtils

@RequestScoped
class SharedRecipeMapper(
  private val authorizationDetails: AuthorizationDetails,
  private val templateService: TemplateService,
  private val flavorMateProperties: FlavorMateProperties,
) : BasicMapper<RecipeEntity, SharedRecipe>() {

  private val server
    get() = flavorMateProperties.server().url()

  override fun mapNotNullBasic(input: RecipeEntity): SharedRecipe {
    return SharedRecipe(
      id = input.id,
      label = input.label,
      cover = input.coverFile?.let { mapCover(input.id) },
      description = input.description,
      prepTime = input.prepTime.takeIf { it.seconds != 0L }?.let { mapDuration(it) },
      cookTime = input.cookTime.takeIf { it.seconds != 0L }?.let { mapDuration(it) },
      restTime = input.restTime.takeIf { it.seconds != 0L }?.let { mapDuration(it) },
      serving = mapServing(input.serving),
      ingredientGroups = input.ingredientGroups.map { mapIngredientGroup(it) },
      instructionGroups = input.instructionGroups.map { mapInstructionGroup(it) },
      diet = mapDiet(input.diet),
      course = mapCourse(input.course),
      categories = input.categories.map { mapCategory(it) },
      tags = input.tags.map { it.label },
      createdOn = mapDateTime(input.createdOn),
      version = input.version,
      url = input.url,
    )
  }

  private fun mapCover(id: String): String {
    val token = authorizationDetails.token
    val quality = ImageWideResolution.W1280.name

    val path =
      UriBuilder.fromResource(ShareController::class.java)
        .path(ShareController::class.java, ShareController::shareFile.name)
        .queryParam("resolution", quality)
        .build(token, id)

    return URI.create(server).resolve(path).toString()
  }

  private fun mapDuration(input: Duration): String {
    return DurationUtils.beautify(input, templateService.messages)
  }

  private fun mapServing(input: ServingEntity): String {
    val labels = listOfNotNull(NumberUtils.beautify(input.amount), input.label)
    return labels.joinToString(" ")
  }

  private fun mapIngredientGroup(input: IngredientGroupEntity): SharedIngredientGroup {
    return SharedIngredientGroup(
      label = input.label,
      ingredients = input.ingredients.map { mapIngredient(it) },
    )
  }

  private fun mapIngredient(input: IngredientEntity): String {
    return listOf(
        input.amount?.let { NumberUtils.beautify(it) },
        input.unit?.getLabel(input.amount),
        input.label,
      )
      .filter(StringUtils::isNotBlank)
      .joinToString(" ")
  }

  private fun mapInstructionGroup(input: InstructionGroupEntity): SharedInstructionGroup {
    return SharedInstructionGroup(
      label = input.label,
      instructions = input.instructions.map { it.label },
    )
  }

  private fun mapDiet(input: Diet): String {
    return when (input) {
      Diet.Meat -> templateService.getMessage { it.diet_meat() }
      Diet.Fish -> templateService.getMessage { it.diet_fish() }
      Diet.Vegetarian -> templateService.getMessage { it.diet_vegetarian() }
      Diet.Vegan -> templateService.getMessage { it.diet_vegan() }
    }
  }

  private fun mapCourse(input: Course): String {
    return when (input) {
      Course.Appetizer -> templateService.getMessage { it.course_appetizer() }
      Course.Dessert -> templateService.getMessage { it.course_dessert() }
      Course.Drink -> templateService.getMessage { it.course_drink() }
      Course.MainDish -> templateService.getMessage { it.course_mainDish() }
      Course.SideDish -> templateService.getMessage { it.course_sideDish() }
      Course.Bakery -> templateService.getMessage { it.course_bakery() }
    }
  }

  private fun mapCategory(input: CategoryEntity): String {
    return input.localizations
      .filter { it.id.language == templateService.locale.language }
      .firstNotNullOf { it.value }
  }

  private fun mapDateTime(input: LocalDateTime): String {
    val formatter =
      DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(templateService.locale)
    return formatter.format(input.toLocalDate())
  }
}
