/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.dao.models

import de.flavormate.features.account.dao.models.AccountEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*
import org.hibernate.annotations.CreationTimestamp

@Entity
@Table(name = "v3__ext__auth__oidc_mapping")
class OIDCMappingEntity : PanacheEntityBase {
  @Id var id: String = UUID.randomUUID().toString()

  @Column(name = "provider_id") lateinit var providerId: String

  lateinit var subject: String

  @Column(name = "account_id") lateinit var accountId: String

  var name: String? = null

  var email: String? = null

  @Column(name = "created_on") @CreationTimestamp var createdOn: LocalDateTime = LocalDateTime.now()

  @ManyToOne
  @JoinColumn(
    name = "account_id",
    referencedColumnName = "id",
    updatable = false,
    insertable = false,
  )
  lateinit var account: AccountEntity

  @ManyToOne
  @JoinColumn(
    name = "provider_id",
    referencedColumnName = "id",
    updatable = false,
    insertable = false,
  )
  lateinit var provider: OIDCProviderEntity

  companion object {
    fun create(
      providerId: String,
      subject: String,
      accountId: String,
      name: String?,
      email: String?,
    ) =
      OIDCMappingEntity().apply {
        this.providerId = providerId
        this.subject = subject
        this.accountId = accountId
        this.name = name
        this.email = email
      }
  }
}
