/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.nextcloudCookbook.services.importer

import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraft
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraftInstructionGroup
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraftServing
import de.flavormate.extensions.importExport.plugins.ldJson.mappers.LDJsonIngredientMapper
import de.flavormate.extensions.importExport.plugins.ldJson.mappers.LDJsonInstructionMapper
import de.flavormate.extensions.importExport.plugins.ldJson.models.LDJsonRecipe
import de.flavormate.extensions.importExport.plugins.ldJson.models.types.LDJsonRestrictedDiet
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.enums.Language
import de.flavormate.shared.extensions.toKebabCase
import de.flavormate.shared.services.LanguageDetectorService
import java.nio.file.Path
import org.apache.commons.lang3.StringUtils

class IEPluginNextcloudCookbookImporter(
  private val languageDetectorService: LanguageDetectorService
) {
  /**
   * Basically the same as
   * [de.flavormate.extensions.importExport.plugins.ldJson.services.importer.IEPluginLDJsonImporter.import],
   * but with a different mapping for instructions, as the Nextcloud Cookbook plugin only supports
   * [de.flavormate.extensions.importExport.plugins.ldJson.models.types.step.LDJsonHowToStep] (Plain
   * instructions, no grouping or nesting) Also only one file is supported per recipe.
   */
  fun import(input: LDJsonRecipe, image: Path?): IERecipeDraft {
    val instructionGroups = LDJsonInstructionMapper.mapInstructionGroups(input.recipeInstructions)

    val lang =
      input.inLanguage?.let { Language.from(it) } ?: getLanguage(instructionGroups) ?: Language.EN

    return IERecipeDraft(
      cookTime = input.cookTime,
      course = null,
      description = input.description ?: input.alternativeHeadline,
      diet = mapDiet(input.suitableForDiet),
      label = input.name ?: input.alternateName,
      prepTime = input.prepTime,
      restTime = null,
      serving = mapServing(input.recipeYield ?: input.yield),
      instructionGroups = instructionGroups,
      ingredientGroups = LDJsonIngredientMapper.mapIngredientGroups(input.recipeIngredient),
      categories = input.recipeCategory.filter(StringUtils::isNotBlank),
      tags = input.keywords.filter(StringUtils::isNotBlank).map { it.toKebabCase() },
      files = listOfNotNull(image?.toFile()),
      url = input.url,
      language = lang,
    )
  }

  private fun getLanguage(input: List<IERecipeDraftInstructionGroup>): Language? {
    val text = input.flatMap { it.instructions }.mapNotNull { it.label }.joinToString("\n")

    return languageDetectorService.getLanguage(text)
  }

  private fun mapDiet(input: LDJsonRestrictedDiet?): Diet? =
    when (input) {
      LDJsonRestrictedDiet.VegetarianDiet -> Diet.Vegetarian
      LDJsonRestrictedDiet.VeganDiet -> Diet.Vegan
      else -> null
    }

  private fun mapServing(input: String?): IERecipeDraftServing {
    if (input == null) return IERecipeDraftServing.empty()

    val parts = input.split("\\s+".toRegex()).filter(StringUtils::isNotBlank)
    if (parts.isEmpty()) return IERecipeDraftServing.empty()

    val amount = parts.first().toDoubleOrNull() ?: return IERecipeDraftServing.empty()
    if (amount <= 0) return IERecipeDraftServing.empty()

    val label = parts.drop(1).joinToString(" ")

    return IERecipeDraftServing(amount = amount, label = label)
  }
}
