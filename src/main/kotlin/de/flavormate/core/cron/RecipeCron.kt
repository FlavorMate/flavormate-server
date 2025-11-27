/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.cron

import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipe.repositories.RecipeNutritionRepository
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.shared.extensions.trimToNull
import de.flavormate.utils.DatabaseUtils
import de.flavormate.utils.URLUtils
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class RecipeCron(
  private val repository: RecipeRepository,
  private val nutritionRepository: RecipeNutritionRepository,
) {

  @Scheduled(cron = "0 0 0 * * ?")
  @Startup
  fun run() {
    Log.info("Start recipe cleanup")

    Log.info("Cleaning up recipe urls")
    cleanRecipeUrls()
    Log.info("Finished cleaning up recipe urls")

    Log.info("Cleaning up recipe descriptions")
    cleanRecipeDescriptions()
    Log.info("Finished cleaning up recipe descriptions")

    Log.info("Cleaning up orphan nutrition")
    cleanOrphanNutrition()
    Log.info("Finished cleaning up orphan nutrition")

    Log.info("Finished recipe cleanup")
  }

  @Transactional
  fun cleanRecipeDescriptions() =
    DatabaseUtils.batchedRun(query = repository.findAllWithDescription()) { items ->
      items.forEach(::cleanRecipeDescription)
    }

  private fun cleanRecipeDescription(recipe: RecipeEntity) {
    val originalDesc = recipe.description ?: return
    val trimmedDesc = originalDesc.trimToNull()

    if (trimmedDesc == originalDesc) return

    recipe.description = trimmedDesc
    repository.persist(recipe)
    Log.info(
      "Recipe ${recipe.label} (${recipe.id}) had an unoptimized description and was optimized"
    )
  }

  @Transactional
  fun cleanRecipeUrls() =
    DatabaseUtils.batchedRun(query = repository.findAllWithUrl()) { items ->
      items.forEach(::cleanRecipeUrl)
    }

  private fun cleanRecipeUrl(recipe: RecipeEntity) {
    val origUrl = recipe.url ?: return

    val cleanUrl = runCatching { URLUtils.cleanURL(origUrl) }.getOrNull()

    if (cleanUrl == origUrl) return

    recipe.url = cleanUrl
    repository.persist(recipe)
    Log.info("Recipe ${recipe.label} (${recipe.id}) had an unoptimized url and was optimized")
  }

  @Transactional
  fun cleanOrphanNutrition() {
    val deletedOrphans = nutritionRepository.deleteOrphan()
    Log.info("Deleted $deletedOrphans orphaned nutrition")
  }
}
