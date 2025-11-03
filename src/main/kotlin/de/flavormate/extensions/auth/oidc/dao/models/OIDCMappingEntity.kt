/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.auth.oidc.dao.models

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*

@Entity
@Table(name = "v3__ext__auth__oidc_mapping")
@IdClass(OIDCMappingEntityId::class)
class OIDCMappingEntity : PanacheEntityBase {
  @Id lateinit var issuer: String

  @Id lateinit var subject: String

  @Id @Column(name = "account_id") lateinit var accountId: String
}
