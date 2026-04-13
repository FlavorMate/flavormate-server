/* Licensed under AGPLv3 2024 - 2026 */
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

  @Column(name = "temporary_file") var temporaryFile: String? = null

  val isTemporary
    get() = temporaryFile != null

  companion object {
    fun create(account: AccountEntity, recipe: RecipeEntity, temporaryFile: String?) =
      RecipeFileEntity().apply {
        this.ownedBy = account
        this.ownedById = account.id
        this.mimeType = MimeTypes.WEBP_MIME
        this.recipe = recipe
        this.temporaryFile = temporaryFile
      }
  }
}
