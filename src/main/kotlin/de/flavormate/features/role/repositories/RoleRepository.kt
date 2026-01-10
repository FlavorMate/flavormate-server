/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.role.repositories

import de.flavormate.features.role.enums.RoleTypes
import de.flavormate.features.role.models.RoleEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RoleRepository : PanacheRepositoryBase<RoleEntity, String> {
  fun findByRole(role: RoleTypes): RoleEntity? {
    return find("role = ?1", role).firstResult()
  }
}
