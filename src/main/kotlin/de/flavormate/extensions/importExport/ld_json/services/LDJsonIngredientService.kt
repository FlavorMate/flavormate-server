/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.importExport.ld_json.services

import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftIngredientGroupItemNutritionEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupItemEntity
import de.flavormate.features.unit.repositories.UnitLocalizedRepository
import de.flavormate.utils.NumberUtils
import jakarta.enterprise.context.ApplicationScoped
import org.apache.commons.lang3.StringUtils

@ApplicationScoped
class LDJsonIngredientService(val unitLocalizedRepository: UnitLocalizedRepository) {

  /**
   * Maps a list of raw ingredient strings into a set of [RecipeDraftIngredientGroupEntity].
   *
   * @param input a list of raw ingredient strings where each string represents an ingredient. Each
   *   string may contain details such as the amount, unit, and label of the ingredient.
   * @return a set of [RecipeDraftIngredientGroupEntity] where each entity contains a set of
   *   processed ingredient drafts parsed and mapped from the input list.
   */
  fun mapIngredientGroupDrafts(
    input: List<String>,
    language: String,
    recipe: RecipeDraftEntity,
  ): MutableList<RecipeDraftIngredientGroupEntity> =
    RecipeDraftIngredientGroupEntity()
      .apply {
        ingredients = input.mapNotNullTo(mutableListOf()) { mapIngredient(it, language, this) }
      }
      .also { it.recipe = recipe }
      .let { mutableListOf(it) }

  /**
   * Maps a raw ingredient input string to an instance of [RecipeDraftIngredientGroupItemEntity].
   *
   * @param input the raw input string representing an ingredient, including its amount, unit, and
   *   label
   * @return an instance of [RecipeDraftIngredientGroupItemEntity] if the input string is
   *   successfully parsed and validated, or `null` if parsing fails
   */
  private fun mapIngredient(
    input: String,
    language: String,
    group: RecipeDraftIngredientGroupEntity,
  ): RecipeDraftIngredientGroupItemEntity? {
    val formatted =
      StringUtils.trimToNull(input)?.let { NumberUtils.convertExtendedFractionString(it) }
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
      this.nutrition = RecipeDraftIngredientGroupItemNutritionEntity()
    }
  }
}
