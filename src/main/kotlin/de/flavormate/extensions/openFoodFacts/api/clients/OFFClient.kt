/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.openFoodFacts.api.clients

import com.fasterxml.jackson.module.kotlin.readValue
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequestBlocking
import de.flavormate.extensions.openFoodFacts.api.models.OpenFoodFactsNutriments
import de.flavormate.extensions.openFoodFacts.api.models.OpenFoodFactsResponse
import de.flavormate.utils.JSONUtils

object OFFClient {
  private fun getUrl(id: String) = "https://world.openfoodfacts.net/api/v2/product/$id.json"

  fun fetchProduct(id: String): OpenFoodFactsNutriments {
    val html = Ksoup.parseGetRequestBlocking(getUrl(id))

    val response = JSONUtils.mapper.readValue<OpenFoodFactsResponse>(html.body().html())
    val nutriments = response.product.nutriments

    return nutriments
  }
}
