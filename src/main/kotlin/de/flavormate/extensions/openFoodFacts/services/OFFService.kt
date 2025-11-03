/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.openFoodFacts.services

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.core.features.enums.FeatureType
import de.flavormate.extensions.openFoodFacts.api.clients.OFFClient
import de.flavormate.extensions.openFoodFacts.api.repository.OFFClientStatsRepository
import de.flavormate.extensions.openFoodFacts.dao.models.OFFProductEntity
import de.flavormate.extensions.openFoodFacts.enums.OFFProductState
import de.flavormate.extensions.openFoodFacts.repositories.OFFProductRepository
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class OFFService(
  private val statsRepository: OFFClientStatsRepository,
  private val productRepository: OFFProductRepository,
  flavorMateProperties: FlavorMateProperties,
) {

  private val openFoodFactsEnabled =
    flavorMateProperties.features()[FeatureType.OpenFoodFacts]!!.enabled()

  @Scheduled(cron = "0 0 * * * ?")
  @Startup
  @Transactional
  fun fetchProducts() {
    if (!openFoodFactsEnabled) return

    val products = productRepository.findByState(OFFProductState.New).page(0, 80).list()

    if (products.isEmpty()) return

    val stat = statsRepository.findCurrent()

    for (product in products) {
      if (stat.requests >= 90) continue

      fetchProductFromOFF(product).also { productRepository.persist(it) }

      stat.also {
        it.requests++
        statsRepository.persist(it)
      }
    }
  }

  private fun fetchProductFromOFF(product: OFFProductEntity): OFFProductEntity {
    try {

      val response = OFFClient.fetchProduct(product.id)

      return product.apply {
        this.carbohydrates = response.carbohydrates
        this.energyKcal = response.energyKcal
        this.fat = response.fat
        this.saturatedFat = response.saturatedFat
        this.sugars = response.sugars
        this.fiber = response.fiber
        this.proteins = response.proteins
        this.salt = response.salt
        this.sodium = response.sodium
        this.state = OFFProductState.Available
      }
    } catch (e: Exception) {
      Log.error("Failed to fetch product from OFF: ${e.message}")
      product.apply { this.state = OFFProductState.Unavailable }
      return product
    }
  }
}
