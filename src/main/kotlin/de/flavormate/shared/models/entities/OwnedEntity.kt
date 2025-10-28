/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.shared.models.entities

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import de.flavormate.features.account.dao.models.AccountEntity
import jakarta.persistence.*

@MappedSuperclass
open class OwnedEntity : TracedEntity() {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owned_by", referencedColumnName = "id")
  @JsonIncludeProperties("id")
  lateinit var ownedBy: AccountEntity

  @Column(name = "owned_by", insertable = false, updatable = false) lateinit var ownedById: String
}
