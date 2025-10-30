/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipeDraft.daos.models

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.category.daos.models.CategoryEntity
import de.flavormate.features.recipeDraft.daos.models.ingredients.RecipeDraftIngredientGroupEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupEntity
import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.models.entities.OwnedEntity
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.persistence.*
import java.time.Duration
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

/**
 * Represents a draft version of a recipe, containing fields that allow for the creation and
 * modification of recipes before they are finalized.
 *
 * This entity maps to the database table `v3_recipe_drafts` and inherits ownership-related
 * properties from the `OwnedEntity` base class.
 *
 * The class supports various properties for storing recipe metadata, preparation details, and
 * associated data such as instructions, ingredients, and categories.
 *
 * Features:
 * - Stores recipe metadata including label, description, and URL.
 * - Duration fields for preparation, cooking, and resting times.
 * - Relationships to associated files and detailed components such as instruction groups,
 *   ingredient groups, and categories.
 * - Tags, course, and dietary information to classify the recipe.
 * - Conversion utilities for handling complex types like instruction groups, ingredient groups,
 *   categories, and serving details to and from JSON for database storage.
 *
 * The `companion object` provides a factory method for creating a default instance of
 * `RecipeDraftEntity` with required associations initialized.
 */
@Entity
@Table(name = "v3__recipe_draft")
@RegisterForReflection
class RecipeDraftEntity : OwnedEntity() {
  @Column(name = "cook_time", precision = 6)
  @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
  var cookTime: Duration = Duration.ZERO

  @Enumerated(EnumType.STRING) var course: Course? = null

  var description: String? = null

  @Enumerated(EnumType.STRING) var diet: Diet? = null

  var label: String? = null

  @Column(name = "prep_time", precision = 6)
  @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
  var prepTime: Duration = Duration.ZERO

  @Column(name = "rest_time", precision = 6)
  @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
  var restTime: Duration = Duration.ZERO

  @ManyToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "serving_id", referencedColumnName = "id", nullable = false)
  lateinit var serving: RecipeDraftServingEntity

  @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], orphanRemoval = true)
  lateinit var instructionGroups: MutableList<RecipeDraftInstructionGroupEntity>

  @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], orphanRemoval = true)
  lateinit var ingredientGroups: MutableList<RecipeDraftIngredientGroupEntity>

  @ManyToMany(mappedBy = "recipeDrafts", cascade = [CascadeType.ALL])
  lateinit var categories: MutableList<CategoryEntity>

  lateinit var tags: MutableList<String>

  @OneToMany(
    cascade = [CascadeType.ALL],
    mappedBy = "recipeDraft",
    fetch = FetchType.LAZY,
    orphanRemoval = true,
  )
  lateinit var files: MutableList<RecipeDraftFileEntity>

  var url: String? = null

  @Column(name = "origin_id") var originId: String? = null

  /**
   * Companion object for the `RecipeDraftEntity` class.
   *
   * Provides factory methods to create new instances of `RecipeDraftEntity`.
   */
  companion object {
    fun create(account: AccountEntity): RecipeDraftEntity =
      RecipeDraftEntity().apply {
        this.ownedBy = account
        this.ownedById = account.id

        this.files = mutableListOf()
        this.serving = RecipeDraftServingEntity.create()
        this.instructionGroups = mutableListOf()
        this.ingredientGroups = mutableListOf()
        this.categories = mutableListOf()
        this.tags = mutableListOf()
      }
  }

  fun generateIndices() {
    instructionGroups.forEachIndexed { index, it ->
      it.index = index
      it.generateIndices()
    }

    ingredientGroups.forEachIndexed { index, it ->
      it.index = index
      it.generateIndices()
    }
  }
}
