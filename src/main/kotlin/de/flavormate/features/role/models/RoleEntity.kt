/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.role.models

import de.flavormate.features.account.dao.models.AccountEntity
import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__role")
class RoleEntity : CoreEntity() {
  @Column(name = "value") @Enumerated(EnumType.STRING) lateinit var role: RoleTypes

  @ManyToMany(mappedBy = "roles") var users: MutableSet<AccountEntity> = mutableSetOf()
}
