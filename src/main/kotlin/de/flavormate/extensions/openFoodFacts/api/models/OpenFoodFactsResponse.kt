/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.openFoodFacts.api.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a response from the OpenFoodFacts API.
 *
 * @param product The product information returned by the OpenFoodFacts API.
 * @param status The status code of the response.
 * @param statusVerbose A verbose description of the status.
 * @param code The barcode of the product.
 */
data class OpenFoodFactsResponse(
  val product: OpenFoodFactsProduct,
  val status: Int,
  @JsonProperty("status_verbose") val statusVerbose: String,
  val code: String,
)
