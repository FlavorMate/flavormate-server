/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.openFoodFacts.api.clients

import com.fasterxml.jackson.module.kotlin.readValue
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequestBlocking
import de.flavormate.configuration.jackson.CustomObjectMapper
import de.flavormate.extensions.openFoodFacts.api.models.OpenFoodFactsNutriments
import de.flavormate.extensions.openFoodFacts.api.models.OpenFoodFactsResponse

object OFFClient {
  private fun getUrl(id: String) = "https://world.openfoodfacts.net/api/v2/product/$id.json"

  fun fetchProduct(id: String): OpenFoodFactsNutriments {
    val html = Ksoup.parseGetRequestBlocking(getUrl(id))

    val response = CustomObjectMapper.instance.readValue<OpenFoodFactsResponse>(html.body().html())
    val nutriments = response.product.nutriments

    return nutriments
  }
}
