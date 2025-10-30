/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.daos.models

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.book.daos.models.BookEntity
import de.flavormate.features.category.daos.models.CategoryEntity
import de.flavormate.features.recipe.daos.models.ingredient.IngredientGroupEntity
import de.flavormate.features.recipe.daos.models.instruction.InstructionGroupEntity
import de.flavormate.features.recipe.daos.models.rating.RatingEntity
import de.flavormate.features.tag.daos.models.TagEntity
import de.flavormate.shared.enums.Course
import de.flavormate.shared.enums.Diet
import de.flavormate.shared.models.entities.OwnedEntity
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.persistence.*
import java.time.Duration
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "v3__recipe")
@RegisterForReflection
class RecipeEntity : OwnedEntity() {
  lateinit var label: String

  @OneToMany(cascade = [CascadeType.ALL], mappedBy = "recipe", fetch = FetchType.LAZY)
  lateinit var files: MutableList<RecipeFileEntity>

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cover_file", referencedColumnName = "id")
  var coverFile: RecipeFileEntity? = null

  var description: String? = null

  @OneToMany(cascade = [CascadeType.ALL], mappedBy = "recipe", fetch = FetchType.LAZY)
  lateinit var ratings: MutableList<RatingEntity>

  @Column(name = "prep_time", precision = 6)
  @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
  lateinit var prepTime: Duration

  @Column(name = "cook_time", precision = 6)
  @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
  lateinit var cookTime: Duration

  @Column(name = "rest_time", precision = 6)
  @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
  lateinit var restTime: Duration

  @ManyToOne(cascade = [CascadeType.ALL])
  @JoinColumn(name = "serving_id", referencedColumnName = "id", nullable = false)
  lateinit var serving: ServingEntity

  @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], orphanRemoval = true)
  lateinit var instructionGroups: MutableList<InstructionGroupEntity>

  @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], orphanRemoval = true)
  lateinit var ingredientGroups: MutableList<IngredientGroupEntity>

  @ManyToMany(mappedBy = "recipes", fetch = FetchType.LAZY)
  lateinit var books: MutableList<BookEntity>

  @ManyToMany(mappedBy = "recipes", cascade = [CascadeType.ALL])
  lateinit var categories: MutableList<CategoryEntity>

  @ManyToMany(mappedBy = "recipes", cascade = [CascadeType.ALL])
  lateinit var tags: MutableList<TagEntity>

  @Enumerated(EnumType.STRING) lateinit var course: Course

  @Enumerated(EnumType.STRING) lateinit var diet: Diet

  var url: String? = null

  fun translate(language: String) {
    for (category in categories) {
      val l10n = category.localizations.first { c -> c.id.language == language }
      category.label = (l10n.value)
    }
  }

  fun toggleBook(book: BookEntity) {
    val bookFound = books.removeIf { it.id == book.id }
    if (!bookFound) {
      books.add(book)
    }
  }

  val totalTime
    get() = Duration.ZERO.plus(prepTime).plus(cookTime).plus(restTime)

  companion object {
    fun create(account: AccountEntity) =
      RecipeEntity().apply {
        this.ownedBy = account
        this.ownedById = account.id

        this.files = mutableListOf()
        this.ratings = mutableListOf()
        this.instructionGroups = mutableListOf()
        this.ingredientGroups = mutableListOf()
        this.books = mutableListOf()
        this.categories = mutableListOf()
        this.tags = mutableListOf()
      }
  }

  fun setCoverFile() {
    coverFile = files.maxByOrNull { it.createdOn }
  }
}
