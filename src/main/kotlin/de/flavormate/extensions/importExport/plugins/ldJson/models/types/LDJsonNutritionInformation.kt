/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ldJson.models.types

data class LDJsonNutritionInformation(
  val calories: String?,
  val carbohydrateContent: String?,
  val cholesterolContent: String?,
  val fatContent: String?,
  val fiberContent: String?,
  val proteinContent: String?,
  val saturatedFatContent: String?,
  val servingSize: String?,
  val sodiumContent: String?,
  val sugarContent: String?,
  val transFatContent: String?,
  val unsaturatedFatContent: String?,
) : LDJsonSchema("NutritionInformation")
