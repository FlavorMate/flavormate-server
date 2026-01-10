/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.book.daos.models

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.recipe.daos.models.RecipeEntity
import de.flavormate.shared.models.entities.OwnedEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "v3__book")
class BookEntity : OwnedEntity() {
  lateinit var label: String

  var visible: Boolean = false

  @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
  @JoinTable(
    name = "v3__book__subscriber",
    joinColumns = [JoinColumn(name = "book_id")],
    inverseJoinColumns = [JoinColumn(name = "account_id")],
  )
  var subscriber: MutableSet<AccountEntity> = mutableSetOf()

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "v3__book__recipe",
    joinColumns = [JoinColumn(name = "book_id")],
    inverseJoinColumns = [JoinColumn(name = "recipe_id")],
  )
  var recipes: MutableSet<RecipeEntity> = mutableSetOf()

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cover_recipe", referencedColumnName = "id")
  var coverRecipe: RecipeEntity? = null

  fun toggleSubscriber(account: AccountEntity) {
    val accountFound = subscriber.removeIf { it.id == account.id }
    if (!accountFound) {
      subscriber.add(account)
    }
  }

  fun toggleRecipe(recipe: RecipeEntity) {
    val recipeFound = recipes.removeIf { it.id == recipe.id }
    if (!recipeFound) {
      recipes.add(recipe)
    }
  }

  override fun hashCode(): Int {
    return Objects.hash(id, label, visible)
  }

  fun setCoverRecipe() {
    coverRecipe = recipes.filter { it.coverFile != null }.maxByOrNull { it.coverFile!!.createdOn }
  }

  companion object {
    fun create(account: AccountEntity, label: String): BookEntity {
      return BookEntity().apply {
        this.ownedBy = account
        this.ownedById = account.id
        this.label = label
        this.visible = false
      }
    }
  }
}
