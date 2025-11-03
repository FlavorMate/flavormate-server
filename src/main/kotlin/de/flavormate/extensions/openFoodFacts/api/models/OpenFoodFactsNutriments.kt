/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.openFoodFacts.api.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import de.flavormate.extensions.openFoodFacts.deserializer.OFFDoubleDeserializer

/**
 * Represents the nutritional information of a product as retrieved from the OpenFoodFacts API.
 *
 * @param carbohydrates The amount of carbohydrates per 100 grams.
 * @param energy_kcal The amount of energy in kilocalories per 100 grams.
 * @param fat The amount of fat per 100 grams.
 * @param fiber The amount of dietary fiber per 100 grams.
 * @param proteins The amount of proteins per 100 grams.
 * @param salt The amount of salt per 100 grams.
 * @param saturatedFat The amount of saturated fat per 100 grams.
 * @param sodium The amount of sodium per 100 grams.
 * @param sugars The amount of sugars per 100 grams.
 */
@JvmRecord
data class OpenFoodFactsNutriments(
  @JsonProperty("carbohydrates_100g")
  @JsonDeserialize(using = OFFDoubleDeserializer::class)
  val carbohydrates: Double?,
  @JsonProperty("energy-kcal_100g")
  @JsonDeserialize(using = OFFDoubleDeserializer::class)
  val energyKcal: Double?,
  @JsonProperty("fat_100g") @JsonDeserialize(using = OFFDoubleDeserializer::class) val fat: Double?,
  @JsonProperty("fiber_100g")
  @JsonDeserialize(using = OFFDoubleDeserializer::class)
  val fiber: Double?,
  @JsonProperty("proteins_100g")
  @JsonDeserialize(using = OFFDoubleDeserializer::class)
  val proteins: Double?,
  @JsonProperty("salt_100g")
  @JsonDeserialize(using = OFFDoubleDeserializer::class)
  val salt: Double?,
  @JsonProperty("saturated-fat_100g")
  @JsonDeserialize(using = OFFDoubleDeserializer::class)
  val saturatedFat: Double?,
  @JsonProperty("sodium_100g")
  @JsonDeserialize(using = OFFDoubleDeserializer::class)
  val sodium: Double?,
  @JsonProperty("sugars_100g")
  @JsonDeserialize(using = OFFDoubleDeserializer::class)
  val sugars: Double?,
)
