/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.exceptions

data class ExceptionDto(
  val request: String,
  val statusCode: Int,
  val statusText: String,
  val message: String,
  val id: String? = null,
)
