/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.tag.daos.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.shared.models.entities.TracedEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__tag")
class TagEntity : TracedEntity() {
  lateinit var label: String

  @ManyToMany
  @JoinTable(
    name = "v3__tag__recipe",
    joinColumns = [JoinColumn(name = "tag_id")],
    inverseJoinColumns = [JoinColumn(name = "recipe_id")],
  )
  @JsonIgnoreProperties("tags")
  var recipes: MutableSet<RecipeEntity> = mutableSetOf()

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cover_recipe", referencedColumnName = "id")
  var coverRecipe: RecipeEntity? = null

  fun addOrRemoveRecipe(recipe: RecipeEntity) {
    if (!recipes.removeIf { r: RecipeEntity -> r.id == recipe.id }) {
      recipes.add(recipe)
    }
  }

  companion object {
    fun create(label: String) = TagEntity().apply { this.label = label }
  }

  fun setCoverRecipe() {
    coverRecipe = recipes.filter { it.coverFile != null }.maxByOrNull { it.coverFile!!.createdOn }
  }
}
