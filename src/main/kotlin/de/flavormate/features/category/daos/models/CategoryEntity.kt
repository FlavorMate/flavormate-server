/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.category.daos.models

import de.flavormate.features.categoryGroup.daos.models.CategoryGroupEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.shared.models.entities.CoreEntity
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "v3__category")
@RegisterForReflection
class CategoryEntity : CoreEntity() {

  lateinit var label: String

  @ManyToOne
  @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
  lateinit var group: CategoryGroupEntity

  @ManyToMany
  @JoinTable(
    name = "v3__category__recipe",
    joinColumns = [JoinColumn(name = "category_id")],
    inverseJoinColumns = [JoinColumn(name = "recipe_id")],
  )
  lateinit var recipes: MutableList<RecipeEntity>

  @ManyToMany
  @JoinTable(
    name = "v3__category__recipe_draft",
    joinColumns = [JoinColumn(name = "category_id")],
    inverseJoinColumns = [JoinColumn(name = "recipe_draft_id")],
  )
  lateinit var recipeDrafts: MutableList<RecipeDraftEntity>

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cover_recipe", referencedColumnName = "id")
  var coverRecipe: RecipeEntity? = null

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  lateinit var localizations: MutableList<CategoryLocalizationEntity>

  fun addOrRemoveRecipe(recipe: RecipeEntity) {
    if (!recipes.removeIf { it.id == recipe.id }) {
      recipes.add(recipe)
    }
  }

  override fun hashCode(): Int {
    return Objects.hash(id, label)
  }

  fun setCoverRecipe() {
    coverRecipe = recipes.filter { it.coverFile != null }.maxByOrNull { it.coverFile!!.createdOn }
  }

  fun translate(language: String) {
    val localization = localizations.first { it.id.language == language }
    label = localization.value
  }
}
