/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.daos.models

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.shared.models.entities.OwnedEntity
import de.flavormate.utils.MimeTypes
import jakarta.persistence.*

@Entity
@Table(name = "v3__recipe__file")
class RecipeFileEntity : OwnedEntity() {

  @Column(name = "mime_type") lateinit var mimeType: String

  @ManyToOne
  @JoinColumn(name = "recipe_id", referencedColumnName = "id")
  lateinit var recipe: RecipeEntity

  var schema: Int = 2

  companion object {
    fun create(
      account: AccountEntity,
      recipe: RecipeEntity,
      mimeType: String = MimeTypes.WEBP_MIME,
    ) =
      RecipeFileEntity().apply {
        this.ownedBy = account
        this.ownedById = account.id
        this.mimeType = mimeType
        this.recipe = recipe
      }
  }
}
