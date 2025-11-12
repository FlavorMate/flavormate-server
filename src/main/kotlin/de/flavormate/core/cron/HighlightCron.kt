/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.core.cron

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.features.highlight.daos.models.HighlightEntity
import de.flavormate.features.highlight.repositories.HighlightRepository
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipe.repositories.RecipeRepository
import de.flavormate.shared.enums.Diet
import io.quarkus.logging.Log
import io.quarkus.runtime.Startup
import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.LocalDate
import kotlin.math.min

@ApplicationScoped
class HighlightCron(
  private val recipeRepository: RecipeRepository,
  private val highlightRepository: HighlightRepository,
  private val flavorMateProperties: FlavorMateProperties,
) {
  val daysToGenerate
    get() = flavorMateProperties.general().highlights().daysToGenerate()

  @Scheduled(cron = "0 0 0 * * ?")
  @Startup
  @Transactional
  fun run() {
    for (diet in Diet.entries) {
      Log.info("Generating highlights for $diet")
      generateHighlightsByDiet(diet)
      Log.info("Finished generating highlights for $diet")

      Log.info("Deleting old highlights for $diet")
      val deletedHighlights = deleteHighlightsByDiet(diet)
      Log.info("Deleted old $deletedHighlights highlights for $diet")
    }
  }

  private fun deleteHighlightsByDiet(diet: Diet): Long {
    val deleteBefore = LocalDate.now().minusDays(daysToGenerate)
    return highlightRepository.deleteOldEntriesById(diet = diet, deleteBefore = deleteBefore)
  }

  private fun generateHighlightsByDiet(diet: Diet) {
    val now = LocalDate.now()

    val newHighlights = mutableListOf<HighlightEntity>()

    val availableRecipes = recipeRepository.countByDietIn(diet.allowedDiets)

    if (availableRecipes == 0L) {
      Log.info("No recipes for $diet available. Skipping!")
      return
    }

    val avoidDuplicatesForDays = min(availableRecipes, daysToGenerate)

    val existingHighlights =
      highlightRepository.findInRangeByDiet(daysToGenerate - 1, diet).associateBy { it.date }

    if (existingHighlights.size >= daysToGenerate) {
      Log.info("Highlights for $diet already generated. Skipping!")
      return
    }

    for (i in daysToGenerate - 1 downTo 0) {
      val currentDate = now.minusDays(i)

      if (existingHighlights.containsKey(currentDate)) continue

      val recentHighlights =
        (existingHighlights + newHighlights.associateBy { it.date })
          .filter { (date, _) -> date.isAfter(currentDate.minusDays(avoidDuplicatesForDays)) }
          .values

      val recipe = getRandomRecipe(diet, recentHighlights)

      newHighlights.add(HighlightEntity.fromRecipe(currentDate, diet, recipe))

      Log.info("Insert ${recipe.label} for $diet on $currentDate")
    }

    highlightRepository.persist(newHighlights)
  }

  private fun getRandomRecipe(
    diet: Diet,
    recentHighlights: Collection<HighlightEntity>,
  ): RecipeEntity {
    val recentIds = recentHighlights.map { it.recipe.id }
    var recipe: RecipeEntity
    do {
      recipe = recipeRepository.findRandomRecipeByDiet(diet.allowedDiets, null).firstResult()!!
    } while (recipe.id in recentIds)

    return recipe
  }
}
