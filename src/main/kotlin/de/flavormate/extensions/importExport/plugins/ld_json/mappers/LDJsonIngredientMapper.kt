/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ld_json.mappers

import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeIngredientGroup
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeIngredientGroupItem

object LDJsonIngredientMapper {

  /**
   * Maps a list of ingredient strings to a list of [IERecipeIngredientGroup] objects.
   *
   * Each input string is processed and transformed into an [IERecipeIngredientGroupItem], which is
   * then grouped together in an [IERecipeIngredientGroup]. This function essentially creates a
   * single ingredient group containing all the mapped ingredient items provided in the input list.
   *
   * @param input A list of raw ingredient strings to be processed. Each string may include details
   *   such as amount, unit, and label (e.g., "1/2 cup sugar").
   * @return A list containing one [IERecipeIngredientGroup] object, with each ingredient string
   *   mapped to a corresponding [IERecipeIngredientGroupItem] within the group.
   */
  fun mapIngredientGroups(input: List<String>): List<IERecipeIngredientGroup> =
    IERecipeIngredientGroup(
        label = null,
        index = 0,
        ingredients = input.mapIndexedNotNull { index, it -> mapIngredient(it, index) },
      )
      .let { listOf(it) }

  /**
   * Maps an input ingredient string to an instance of [IERecipeIngredientGroupItem].
   *
   * Parses and processes the input string to extract ingredient properties such as the amount,
   * unit, and label. Handles extended fraction strings to compute numeric values for amounts. Trims
   * whitespace and null values from the input to construct the resulting object.
   *
   * @param input The raw ingredient string to be mapped. This may include numeric values, units,
   *   and descriptions (e.g., "1/2 cup sugar").
   * @param index The index of the ingredient in the recipe list, used to track its order.
   * @return An instance of [IERecipeIngredientGroupItem] containing the mapped properties, or null
   *   if the input is invalid or properties cannot be extracted.
   */
  private fun mapIngredient(input: String, index: Int): IERecipeIngredientGroupItem? {
    if (input.isBlank()) return null

    return IERecipeIngredientGroupItem(index = index, label = input, nutrition = null)
  }
}
