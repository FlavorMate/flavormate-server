/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.token.daos

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.token.enums.TokenType
import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.*
import java.time.Duration
import java.time.LocalDateTime

@Entity
@Table(name = "v3__token")
class TokenEntity : CoreEntity() {
  @Column(name = "issued_at") var issuedAt: LocalDateTime = LocalDateTime.now()

  @Column(name = "expired_at") var expiredAt: LocalDateTime? = null

  var revoked: Boolean = false

  @Enumerated(EnumType.STRING) lateinit var type: TokenType

  /**
   * Stores the id of the resource it should protect Dependent on [type]
   * - [TokenType.BRING]: recipe uuid
   * - [TokenType.PASSWORD]: account uuid
   * - [TokenType.RESET]: account uuid
   * - [TokenType.SHARE]: recipe uuid
   */
  @Column(name = "secured_resource") lateinit var securedResource: String

  @Column(name = "owned_by", insertable = false, updatable = false) lateinit var ownedById: String

  /** Stores the uuid of the issuer. If null, the backend issued the token (e.g. login token) */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owned_by", referencedColumnName = "id")
  var issuer: AccountEntity? = null

  val valid
    get(): Boolean {
      if (revoked) return false
      if (expiredAt != null && expiredAt!!.isBefore(LocalDateTime.now())) return false
      return true
    }

  companion object {
    fun create(
      hashedToken: String,
      type: TokenType,
      securedResource: String,
      issuer: AccountEntity?,
      duration: Duration?,
    ): TokenEntity =
      TokenEntity().apply {
        this.id = hashedToken
        this.type = type
        this.securedResource = securedResource
        this.issuer = issuer
        this.issuedAt = LocalDateTime.now()
        this.expiredAt = duration?.let { LocalDateTime.now().plus(it) }
      }
  }
}
