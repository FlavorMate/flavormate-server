/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.repositories

import de.flavormate.features.recipe.daos.models.RecipeFileEntity
import de.flavormate.shared.interfaces.CRepository
import io.quarkus.panache.common.Page
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecipeFileRepository : CRepository<RecipeFileEntity>(RecipeFileEntity::class) {

  fun findAllTemporary(limit: Int): List<String> {
    val page = Page.ofSize(limit)
    return find("select id from RecipeFileEntity where temporaryFile is not null")
      .page(page)
      .project(String::class.java)
      .list()
  }

  fun updateDeleteTemporary(id: String): Int {
    val params = mapOf("id" to id)

    return update("temporaryFile = null where id = :id", params)
  }
}
