/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.repositories

import de.flavormate.features.recipe.daos.models.RecipeFileEntity
import de.flavormate.shared.interfaces.CRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecipeFileRepository : CRepository<RecipeFileEntity>(RecipeFileEntity::class)
