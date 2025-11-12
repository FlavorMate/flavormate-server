/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.cron

import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.utils.URLUtils
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class RecipeCron(private val repository: RecipeRepository) {

  @Scheduled(cron = "0 0 0 * * ?")
  @Startup
  @Transactional
  fun run() {
    var pageIndex = 0
    var hasMore = true

    while (hasMore) {
      val page = repository.findAllWithUrl().page(pageIndex, 10)
      val items = page.list()
      hasMore = page.hasNextPage()

      items.forEach(::cleanRecipeUrl)

      pageIndex++
    }
  }

  private fun cleanRecipeUrl(recipe: RecipeEntity) {
    val url = recipe.url ?: return

    try {
      val cleanedUrl = URLUtils.cleanURL(url)
      if (cleanedUrl != url) {
        recipe.url = cleanedUrl
        repository.persist(recipe)
        Log.info("Recipe ${recipe.label} (${recipe.id}) had an unoptimized url and was optimized")
      }
    } catch (_: Exception) {
      recipe.url = null
      Log.info(
        "Recipe ${recipe.label} (${recipe.id}) had an invalid url and was set to null: `$url`"
      )
    }
  }
}
