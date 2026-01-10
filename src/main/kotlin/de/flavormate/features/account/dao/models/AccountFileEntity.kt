/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.account.dao.models

import de.flavormate.shared.models.entities.OwnedEntity
import de.flavormate.utils.MimeTypes
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "v3__account__file")
class AccountFileEntity : OwnedEntity() {
  @Column(name = "mime_type") lateinit var mimeType: String

  var schema: Int = 2

  companion object {
    fun create(account: AccountEntity): AccountFileEntity {
      return AccountFileEntity().apply {
        this.ownedBy = account
        this.ownedById = account.id
        this.mimeType = MimeTypes.WEBP_MIME
      }
    }
  }
}
