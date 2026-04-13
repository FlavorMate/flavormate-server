/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipeDraft.daos.models

import com.fasterxml.jackson.annotation.JsonIgnore
import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.shared.models.entities.OwnedEntity
import de.flavormate.utils.MimeTypes
import jakarta.persistence.*

@Entity
@Table(name = "v3__recipe_draft__file")
class RecipeDraftFileEntity : OwnedEntity() {

  @Column(name = "mime_type") lateinit var mimeType: String

  @Column(name = "origin_id") var originId: String? = null

  var schema: Int = 2

  @Column(name = "temporary_file") var temporaryFile: String? = null

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "recipe_draft_id", referencedColumnName = "id")
  lateinit var recipeDraft: RecipeDraftEntity

  val isTemporary
    get() = temporaryFile != null

  companion object {
    fun create(
      account: AccountEntity,
      recipeDraft: RecipeDraftEntity,
      temporaryFile: String?,
    ): RecipeDraftFileEntity {
      return RecipeDraftFileEntity().apply {
        this.ownedBy = account
        this.ownedById = account.id
        this.recipeDraft = recipeDraft
        this.mimeType = MimeTypes.WEBP_MIME
        this.temporaryFile = temporaryFile
      }
    }

    fun createFromRecipe(
      account: AccountEntity,
      recipeDraft: RecipeDraftEntity,
      originId: String,
    ): RecipeDraftFileEntity = create(account, recipeDraft, null).apply { this.originId = originId }
  }
}
