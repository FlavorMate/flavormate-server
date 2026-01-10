/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.ratings.dtos

data class RatingsDto(
  val recipeId: String,
  val star1: Long,
  val star2: Long,
  val star3: Long,
  val star4: Long,
  val star5: Long,
  val total: Long,
  val average: Double,
  val ownRating: Double?,
)
