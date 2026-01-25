/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.auth.dao.models

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.shared.models.entities.TracedEntity
import jakarta.persistence.*
import java.time.Duration
import java.time.LocalDateTime

@Entity
@Table(name = "v3__account__session")
class SessionEntity : TracedEntity() {

  @Column(name = "token_hash") lateinit var tokenHash: String

  @Column(name = "expires_at") lateinit var expiresAt: LocalDateTime

  var revoked: Boolean = false

  @Column(name = "account_id") lateinit var accountId: String

  @Column(name = "user_agent") var userAgent: String? = null

  @ManyToOne
  @JoinColumn(
    name = "account_id",
    referencedColumnName = "id",
    insertable = false,
    updatable = false,
  )
  lateinit var account: AccountEntity

  companion object {
    fun create(
      tokenHash: String,
      expiresIn: Duration,
      account: AccountEntity,
      userAgent: String?,
    ): SessionEntity {
      return SessionEntity().apply {
        this.tokenHash = tokenHash
        this.expiresAt = LocalDateTime.now().plus(expiresIn)
        this.revoked = false
        this.accountId = account.id
        this.userAgent = userAgent
      }
    }
  }

  fun update(tokenHash: String, expiresIn: Duration, userAgent: String?) {
    this.tokenHash = tokenHash
    this.expiresAt = LocalDateTime.now().plus(expiresIn)
    this.userAgent = userAgent
  }
}
