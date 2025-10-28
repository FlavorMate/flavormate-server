/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.account.repositories

import de.flavormate.features.account.dao.models.AccountFileEntity
import de.flavormate.shared.interfaces.CRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AccountFileRepository : CRepository<AccountFileEntity>(AccountFileEntity::class) {

  fun deleteByOwnedById(id: String): Boolean {
    return delete("ownedById = ?1", id) > 0
  }

  fun findByOwnedById(id: String): AccountFileEntity? {
    return find("ownedById = ?1", id).firstResult()
  }
}
